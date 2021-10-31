package org.quitian14.tyba.technicaltest.security

import org.quitian14.tyba.technicaltest.model.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class UserSecurity {
    @Autowired
    lateinit var securityService: JWTTokenManager

    @Throws(Exception::class)
    fun getUser(request: HttpServletRequest): User {
        val bearer = request.getHeader("Authorization") ?: throw SecurityException("Token required")

        if (!bearer.startsWith(prefix = "Bearer")) {
            throw SecurityException("Invalid token")
        }
        val token = bearer.substring(7)
        return securityService.validateToken(token)
    }
}