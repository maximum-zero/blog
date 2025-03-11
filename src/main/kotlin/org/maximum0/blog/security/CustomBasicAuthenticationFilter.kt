package org.maximum0.blog.security

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.maximum0.blog.domain.member.MemberRepository
import org.maximum0.blog.exception.MemberNotFoundException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class CustomBasicAuthenticationFilter(
    private val memberRepository: MemberRepository,
    authenticationManager: AuthenticationManager
): BasicAuthenticationFilter(authenticationManager) {

    val log = KotlinLogging.logger {  }
    private val jwtManager = JwtManager()

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        log.info { "권한이나 인증이 필요한 요청이 들어옴" }
        val token = request.getHeader(jwtManager.jwtHeader)?.replace("Bearer ", "")

        log.debug { "token: $token" }

        if (token == null) {
            chain.doFilter(request, response)
            return
        }

        val email = jwtManager.getMemberEmail(token) ?: throw RuntimeException("Member Email을 찾을 수 없습니다.")
        val member = memberRepository.findMemberByEmail(email) ?: throw MemberNotFoundException(email)
        val principalDetails = PrincipalDetails(member)
        val authentication = UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.authorities)

        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }


}