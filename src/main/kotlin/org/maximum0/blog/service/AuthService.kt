package org.maximum0.blog.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.maximum0.blog.domain.member.MemberRepository
import org.maximum0.blog.exception.MemberNotFoundException
import org.maximum0.blog.security.PrincipalDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val memberRepository: MemberRepository
): UserDetailsService {

    val log = KotlinLogging.logger {  }

    override fun loadUserByUsername(email: String): UserDetails {

        log.info { "loadUserByUsername 호출" }

        val member = memberRepository.findMemberByEmail(email) ?: throw MemberNotFoundException(email)
        return PrincipalDetails(member)
    }

}