package org.maximum0.blog.filter

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest

class AuthFilter : Filter {

    val logger = KotlinLogging.logger {  }
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val servletRequest = request as HttpServletRequest
        val principal = servletRequest.session.getAttribute("principal")

        logger.info { "principal: $principal" }

        if (principal == null) {
            throw RuntimeException("Session Not Found")
        } else {
            chain?.doFilter(request, response)
        }
    }


}