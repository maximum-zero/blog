package org.maximum0.blog.config.security

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import org.maximum0.blog.domain.member.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@JsonIgnoreProperties(ignoreUnknown = true)
class PrincipalDetails(
    var member: Member = Member.createFakeMember(0L)
) : UserDetails {

    private val log = KotlinLogging.logger {  }

    @JsonIgnore
    val collection: MutableList<GrantedAuthority> = ArrayList()

    init {
        this.collection.add(GrantedAuthority { "ROLE_" + member.role } )
    }

    @JsonIgnore
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        log.info { "Role 검증" }
//        val collection: MutableCollection<GrantedAuthority> = ArrayList()
//        collection.add(GrantedAuthority { "ROLE_" + member.role })
        return this.collection
    }

    override fun getPassword(): String {
        return member.password
    }

    override fun getUsername(): String {
        return member.email
    }

}