package org.quitian14.tyba.technicaltest.config

import org.quitian14.tyba.technicaltest.security.SecurityInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class InterceptorConfig : WebMvcConfigurer {

    @Autowired
    private lateinit var securityInterceptor: SecurityInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(securityInterceptor)
    }
}
