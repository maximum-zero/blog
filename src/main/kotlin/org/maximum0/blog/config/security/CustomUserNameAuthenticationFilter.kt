package org.maximum0.blog.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.maximum0.blog.domain.member.LoginDto
import org.maximum0.blog.util.CookieProvider
import org.maximum0.blog.util.func.responseData
import org.maximum0.blog.util.value.ComResDto
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.concurrent.TimeUnit

class CustomUserNameAuthenticationFilter(
    private val objectMapper: ObjectMapper
) : UsernamePasswordAuthenticationFilter() {
    
    private val log = KotlinLogging.logger {  }
    private val jwtManager = JwtManager()


    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        
        log.info { "login 요청" }

        lateinit var loginDto: LoginDto

        try {
            loginDto = objectMapper.readValue(request?.inputStream, LoginDto::class.java)
            log.info { "LoginDto $loginDto"}

        } catch (e: Exception) {
            log.error { "LoginFilter: 로그인 요청 DTO 생성 실패" }
        }

        val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.email, loginDto.password)
        return this.authenticationManager.authenticate(authenticationToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        log.info { "Login Complete And Create JWT Token" }
        val principalDetails = authResult?.principal as PrincipalDetails
        val accessToken = jwtManager.generateAccessToken(objectMapper.writeValueAsString(principalDetails))
        val refreshToken = jwtManager.generateRefreshToken(objectMapper.writeValueAsString(principalDetails))
        val refreshCookie = CookieProvider.createCookie(CookieProvider.REFRESH_COOKIE, refreshToken, TimeUnit.DAYS.toSeconds(jwtManager.refreshTokenExpireDay))

        response.addHeader(jwtManager.authorizationHeader, jwtManager.jwtHeader + accessToken)
//        response.addHeader("refreshToken", jwtManager.jwtHeader + refreshToken)
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString())

        val jsonResult = objectMapper.writeValueAsString(ComResDto(HttpStatus.OK, "Login Success", principalDetails.member))

        responseData(response, jsonResult)
    }
}