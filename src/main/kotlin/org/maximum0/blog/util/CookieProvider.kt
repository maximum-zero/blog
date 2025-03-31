package org.maximum0.blog.util

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseCookie
import java.util.Optional

object CookieProvider {

    private val log = KotlinLogging.logger {  }

    fun createNullCookie(cookieName: String): String {
        TODO()
    }

    fun createCookie(cookieName: CookieName, value: String, maxAge: Long): ResponseCookie {
        return ResponseCookie
            .from(cookieName.name, value)
            .httpOnly(true)
            .secure(false)  // HTTPS 일 때 활성화
            .path("/")
            .maxAge(maxAge)
            .build()
    }

    fun getCookie(req: HttpServletRequest, cookieName: CookieName): Optional<String> {
        val cookieValue = req.cookies.filter { it ->
            it.name == cookieName.name
        }.map { it ->
            it.value
        }.firstOrNull()

        log.info { "cookieValue => $cookieValue" }
        return Optional.ofNullable(cookieValue)
    }

    enum class CookieName {
        REFRESH_COOKIE
    }

    val REFRESH_COOKIE = CookieName.REFRESH_COOKIE

}