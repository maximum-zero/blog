package org.maximum0.blog.security

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.maximum0.blog.domain.member.LoginDto
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

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
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        log.info { "Login Complete And Create JWT Token" }
        val principalDetails = authResult?.principal as PrincipalDetails
        val jwtToken = jwtManager.generateAccessToken(principalDetails)
        response?.addHeader(jwtManager.jwtHeader, "Bearer $jwtToken")
    }
}