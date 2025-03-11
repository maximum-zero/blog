package org.maximum0.blog.util

import io.github.oshai.kotlinlogging.KotlinLogging
import org.maximum0.blog.domain.member.Member
import org.maximum0.blog.security.JwtManager
import org.maximum0.blog.security.PrincipalDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import kotlin.test.Test

class UtilTest {

    val log = KotlinLogging.logger {  }

    @Test
    fun generateJWTTest() {
        val jwtManager = JwtManager()

        val details = PrincipalDetails(Member.createFakeMember(1))
        val accessToken = jwtManager.generateAccessToken(details)

        val email = jwtManager.getMemberEmail(accessToken ?: "")

        log.info { "AccessToken $accessToken" }
        log.info { "Email $email" }
    }

    @Test
    fun bcryptEncodeTest() {
        val encoder = BCryptPasswordEncoder()
        val encPassword = encoder.encode("1234")
        log.info { "encPassword > $encPassword" }
    }

}