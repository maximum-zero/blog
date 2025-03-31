package org.maximum0.blog.config.security

import com.auth0.jwt.exceptions.TokenExpiredException
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.maximum0.blog.domain.member.MemberRepository
import org.maximum0.blog.util.CookieProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class CustomBasicAuthenticationFilter(
    private val memberRepository: MemberRepository,
    authenticationManager: AuthenticationManager,
    private val mapper: ObjectMapper
): BasicAuthenticationFilter(authenticationManager) {

    val log = KotlinLogging.logger {  }
    private val jwtManager = JwtManager()

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        log.info { "권한이나 인증이 필요한 요청이 들어옴" }

        val accessToken = request.getHeader(jwtManager.authorizationHeader)?.replace("Bearer ", "")
        if (accessToken == null) {
            chain.doFilter(request, response)
            return
        }

        log.debug { "access token: $accessToken" }

        val accessTokenResult = jwtManager.validAccessToken(accessToken)
        if (accessTokenResult is TokenValidResult.Failure) {
            handleTokenException(accessTokenResult) {
                log.info { "getClass ==> ${accessTokenResult.exception.javaClass}" }

                val refreshToken = CookieProvider.getCookie(request, CookieProvider.REFRESH_COOKIE).orElseThrow()
                val refreshTokenResult = jwtManager.validRefreshToken(refreshToken)
                if (refreshTokenResult is TokenValidResult.Failure) {
                    throw RuntimeException("invalid refresh token")
                }

                val principalString = jwtManager.getPrincipalByRefreshToken(refreshToken)
                val principalDetails = mapper.readValue(principalString, PrincipalDetails::class.java)

                reissueAccessToken(principalDetails, response)
                setAuthentication(principalDetails, chain, request, response)
            }
            return
        }

        val principalJsonData = jwtManager.getPrincipalByAccessToken(accessToken)
        val principalDetails = mapper.readValue(principalJsonData, PrincipalDetails::class.java)
        setAuthentication(principalDetails, chain, request, response)
    }

    private fun setAuthentication(
        principalDetails: PrincipalDetails,
        chain: FilterChain,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        val authentication = UsernamePasswordAuthenticationToken(
            principalDetails,
            principalDetails.password,
            principalDetails.authorities
        )
        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }

    private fun reissueAccessToken(
        principalDetails: PrincipalDetails?,
        response: HttpServletResponse
    ) {
        log.info { "access token 재발급" }
        
        val newAccessToken = jwtManager.generateAccessToken(mapper.writeValueAsString(principalDetails))
        response.addHeader(jwtManager.authorizationHeader, jwtManager.jwtHeader + newAccessToken)
    }

    private fun handleTokenException (tokenValidResult: TokenValidResult.Failure, func: () -> Unit) {
        when(tokenValidResult.exception) {
            is TokenExpiredException -> func()
            else -> {
                log.error { tokenValidResult.exception.stackTraceToString() }
                throw tokenValidResult.exception
            }
        }
    }

}