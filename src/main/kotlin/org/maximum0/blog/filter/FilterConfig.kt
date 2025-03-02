package org.maximum0.blog.filter

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {

    @Bean
    fun registryAuthFilter(): FilterRegistrationBean<AuthFilter> {
        val bean = FilterRegistrationBean(AuthFilter())
        bean.addUrlPatterns("/member/*")
        bean.order = 0
        return bean
    }
}