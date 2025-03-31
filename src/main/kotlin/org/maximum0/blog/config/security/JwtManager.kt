package org.maximum0.blog.config.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import org.maximum0.blog.util.CookieProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*
import java.util.concurrent.TimeUnit

class JwtManager (
    val accessTokenExpireSeconds: Long = 10,
    val refreshTokenExpireDay: Long = 7
) {

    private val log = KotlinLogging.logger {  }

    private val accessSecretKey = "ACCESS_KEY"
    private val refreshSecretKey = "REFRESH_KEY"
    private val claimEmail = "email"
    private val claimPassword = "password"
    val claimPrincipal = "principal"

    val jwtHeader = "Bearer "
    val authorizationHeader = "Authorization"
    private val jwtSubject = "my-token"

    fun generateRefreshToken(principal: String): String {
        val expireDate = Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(refreshTokenExpireDay))
        log.info { "refreshToken ExpireDate > $expireDate" }

        return doGenerateToken(expireDate, principal, refreshSecretKey)
    }

    fun generateAccessToken(principal: String): String {
        val expireDate = Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(accessTokenExpireSeconds))
        log.info { "accessToken ExpireDate > $expireDate" }

        return doGenerateToken(expireDate, principal, accessSecretKey)
    }

    private fun doGenerateToken(
        expireDate: Date,
        principal: String,
        secretKey: String
    ): String = JWT.create()
        .withSubject(jwtSubject)
        .withExpiresAt(expireDate)
        .withClaim(claimPrincipal, principal)
        .sign(Algorithm.HMAC512(secretKey))

    fun getMemberEmail(token: String): String? {
        return JWT.require(Algorithm.HMAC512(accessSecretKey)).build().verify(token)
            .getClaim(claimEmail).asString()
    }

    fun getPrincipalByAccessToken(accessToken: String): String {
        val decodedJWT = getDecodeJwt(accessSecretKey, accessToken)
        return decodedJWT.getClaim(claimPrincipal).asString()
    }

    fun getPrincipalByRefreshToken(refreshToken: String): String {
        val decodedJWT = getDecodeJwt(refreshSecretKey, refreshToken)
        return decodedJWT.getClaim(claimPrincipal).asString()
    }

    private fun getDecodeJwt(secretKey: String, token: String): DecodedJWT {
        val verifier: JWTVerifier = JWT.require(Algorithm.HMAC512(secretKey))
            .build()
        val decodedJWT: DecodedJWT = verifier.verify(token)
        return decodedJWT
    }

    fun validAccessToken(token: String): TokenValidResult {
        return validatedJwt(accessSecretKey, token)
    }
    fun validRefreshToken(token: String): TokenValidResult {
        return validatedJwt(refreshSecretKey, token)
    }

    // Return > TRUE | JWTVerificationException
    fun validatedJwt(secretKey: String, token: String): TokenValidResult {
        try {
            val decodedJWT = getDecodeJwt(secretKey, token)
            return TokenValidResult.Success(decodedJWT = decodedJWT)
        } catch (e: JWTVerificationException) {
            log.error { "validated JWT error" }
//            log.error { "error > ${e.printStackTrace()}"}
            return TokenValidResult.Failure(e)
        }
    }


//    private fun reissueAccessToken(
//        e: JWTVerificationException,
//        req: HttpServletRequest?
//    ) {
//        if (e is TokenExpiredException) {
//            val refreshToken = CookieProvider.getCookie(req!!, "refreshToken").orElseThrow()
//            val validatedJWT = validatedJwt(refreshToken)
//
//            val principalString = getPrincipalByAccessToken(refreshToken)
//            val principalDetails = ObjectMapper().readValue(principalString, PrincipalDetails::class.java)
//
//            val authentication = UsernamePasswordAuthenticationToken(
//                principalDetails,
//                principalDetails.password,
//                principalDetails.authorities
//            )
//
//            SecurityContextHolder.getContext().authentication = authentication
//        }
//    }

}

/**
 * 코틀린으로 Union Type 흉내
 */
sealed class TokenValidResult {
    class Success(val successValue: Boolean = true, val decodedJWT: DecodedJWT) : TokenValidResult()
    class Failure(val exception: JWTVerificationException) : TokenValidResult()
}