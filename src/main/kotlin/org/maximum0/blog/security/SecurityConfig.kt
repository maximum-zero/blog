package org.maximum0.blog.security

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.maximum0.blog.domain.member.MemberRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity(debug = false)
class SecurityConfig(
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val objectMapper: ObjectMapper,
    private val memberRepository: MemberRepository,
) {

    private val log = KotlinLogging.logger {  }

//    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer? {
        return WebSecurityCustomizer { web: WebSecurity -> web.ignoring().requestMatchers("/**")}
    }

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http
            .csrf { it.disable() }
            .headers { it.frameOptions { it.disable() }}
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)}
            .cors { it.configurationSource(corsConfig()) }
            .addFilter(loginFilter())
            .addFilter(authenticationFilter())
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/**").authenticated()  // 인증된 사용자만 접근
//                    .anyRequest().permitAll()  // 그 외 모든 요청은 허용
            }
            .exceptionHandling {
                it.accessDeniedHandler(CustomAccessDeniedHandler())
                it.authenticationEntryPoint(CustomAuthenticationEntryPoint())
            }
            .anonymous { it.disable() }

        return http.build()
    }

    class CustomAuthenticationEntryPoint: AuthenticationEntryPoint {

        private val log = KotlinLogging.logger {  }

        override fun commence(
            request: HttpServletRequest?,
            response: HttpServletResponse?,
            authException: AuthenticationException?
        ) {
            log.info { "not auth!!!" }
            response?.sendError(HttpServletResponse.SC_UNAUTHORIZED)
        }
    }

    class CustomAccessDeniedHandler: AccessDeniedHandler {

        private val log = KotlinLogging.logger {  }

        override fun handle(
            request: HttpServletRequest?,
            response: HttpServletResponse?,
            accessDeniedException: AccessDeniedException?
        ) {
            log.info { "access denied!!! > " }
            response?.sendError(HttpServletResponse.SC_FORBIDDEN)
        }
    }

    class CustomSuccessHandler : AuthenticationSuccessHandler {

        private val log = KotlinLogging.logger {  }

        override fun onAuthenticationSuccess(
            request: HttpServletRequest?,
            response: HttpServletResponse?,
            authentication: Authentication?
        ) {
            log.info { "로그인 성공" }

        }
    }

    class CustomFailureHandler : AuthenticationFailureHandler {

        private val log = KotlinLogging.logger {  }

        override fun onAuthenticationFailure(
            request: HttpServletRequest?,
            response: HttpServletResponse?,
            exception: AuthenticationException?
        ) {
            log.warn { "로그인 실패" }

            response?.sendError(HttpServletResponse.SC_FORBIDDEN, "인증 실패")
        }
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun loginFilter(): CustomUserNameAuthenticationFilter {
        val authenticationFilter = CustomUserNameAuthenticationFilter(objectMapper)
        authenticationFilter.setAuthenticationManager(authenticationManager())
        authenticationFilter.setFilterProcessesUrl("/login")
        authenticationFilter.setAuthenticationSuccessHandler(CustomSuccessHandler())
        authenticationFilter.setAuthenticationFailureHandler(CustomFailureHandler())

        return authenticationFilter
    }

    @Bean
    fun authenticationFilter(): CustomBasicAuthenticationFilter {
        return CustomBasicAuthenticationFilter(authenticationManager = authenticationManager(), memberRepository = memberRepository)
    }

    fun corsConfig(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOriginPattern("*")
        config.addAllowedMethod("*")
        config.addAllowedHeader("*")
        config.addExposedHeader("authorization")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)

        return source
    }

}