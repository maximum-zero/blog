package org.maximum0.blog.security

import io.github.oshai.kotlinlogging.KotlinLogging
import org.maximum0.blog.domain.member.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class PrincipalDetails(
    val member: Member
) : UserDetails {

    private val log = KotlinLogging.logger {  }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        log.info { "Role 검증" }
        val collection: MutableCollection<GrantedAuthority> = ArrayList()
        collection.add(GrantedAuthority { "ROLE_" + member.role })
        return collection
    }

    override fun getPassword(): String {
        return member.password
    }

    override fun getUsername(): String {
        return member.email
    }

}