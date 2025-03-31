package org.maximum0.blog.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.github.oshai.kotlinlogging.KotlinLogging
import org.maximum0.blog.domain.member.Member
import org.maximum0.blog.config.security.JwtManager
import org.maximum0.blog.config.security.PrincipalDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import kotlin.test.Test

@SpringBootTest
class UtilTest @Autowired constructor(
    private val objectMapper: ObjectMapper
) {

    val log = KotlinLogging.logger {  }

    @Test
    fun generateJWTTest() {
//        objectMapper.registerModule(JavaTimeModule())
//            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

        val jwtManager = JwtManager(1)

        val details = PrincipalDetails(Member.createFakeMember(1))
        val jsonPrincipal = objectMapper.writeValueAsString(details)
        val accessToken = jwtManager.generateAccessToken(jsonPrincipal)

        Thread.sleep(5000)

        val decodedJWT = jwtManager.validatedJwt(accessToken)

        val principalString = decodedJWT.getClaim(jwtManager.claimPrincipal).asString()
        val principalDetails = objectMapper.readValue(principalString, PrincipalDetails::class.java)

        log.info { "result > ${principalDetails.member}" }

        details.authorities.forEach {
            println(it.authority)
        }

        principalDetails.authorities.forEach {
            println(it.authority)
        }
    }

    @Test
    fun bcryptEncodeTest() {
        val encoder = BCryptPasswordEncoder()
        val encPassword = encoder.encode("1234")
        log.info { "encPassword > $encPassword" }
    }

}