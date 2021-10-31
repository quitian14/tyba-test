package org.quitian14.tyba.technicaltest.controllers

import org.quitian14.tyba.technicaltest.constants.Routes
import org.quitian14.tyba.technicaltest.model.entities.User
import org.quitian14.tyba.technicaltest.model.entities.UserTransaction
import org.quitian14.tyba.technicaltest.model.request.CreateUserRequest
import org.quitian14.tyba.technicaltest.model.request.LoginRequest
import org.quitian14.tyba.technicaltest.security.Secured
import org.quitian14.tyba.technicaltest.security.UserSecurity
import org.quitian14.tyba.technicaltest.services.SecurityService
import org.quitian14.tyba.technicaltest.services.UserService
import org.quitian14.tyba.technicaltest.services.UserTransactionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(Routes.USER)
class UserController {

    @Autowired
    lateinit var securityService: SecurityService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var userTransactionService: UserTransactionService

    @Autowired
    lateinit var userSecurity: UserSecurity

    @PostMapping(Routes.LOGIN)
    fun login(@RequestBody @Validated loginRequest: LoginRequest) =
        securityService.login(loginRequest.user, loginRequest.password)


    @Secured(permissions = ["create"])
    @PostMapping
    fun create(@RequestBody @Validated req: CreateUserRequest) {
        val user = User(req.userName, req.mail, req.name, req.password)

        userService.create(user)
    }

    @Secured(permissions = ["get"])
    @GetMapping(Routes.USER_TRANSACTION)
    fun userTransactions(request: HttpServletRequest): List<UserTransaction> {
        val user = userSecurity.getUser(request)

        return userTransactionService.findByUser(user.userName)
    }
}