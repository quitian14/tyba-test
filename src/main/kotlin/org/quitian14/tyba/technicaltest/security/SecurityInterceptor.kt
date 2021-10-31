package org.quitian14.tyba.technicaltest.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SecurityInterceptor : HandlerInterceptor {

    @Autowired
    lateinit var userSecurity: UserSecurity

    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod) {
            val securityFilter: Secured = handler.method.getAnnotation(Secured::class.java) ?: return true

            val user = userSecurity.getUser(request)

            val permissions = user.permissions
            val handlerPermissions = securityFilter.permissions

            if (permissions!!.isEmpty()) return true

            if (handlerPermissions.intersect(permissions).isEmpty()) {
                throw SecurityException("User has not access to this function.")
            }
        }

        return true
    }

    @Throws(Exception::class)
    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        super.postHandle(request, response, handler, modelAndView)
    }

    @Throws(Exception::class)
    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        exception: java.lang.Exception?
    ) {
        println("After completion of request and response")
    }
}
