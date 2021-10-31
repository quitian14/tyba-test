package org.quitian14.tyba.technicaltest.services

import org.quitian14.tyba.technicaltest.exceptions.BusinessException
import org.quitian14.tyba.technicaltest.model.responses.LoginResponse
import org.quitian14.tyba.technicaltest.repositories.UserRepository
import org.quitian14.tyba.technicaltest.security.JWTTokenManager
import org.quitian14.tyba.technicaltest.utils.EncryptorUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SecurityService {

    @Autowired
    lateinit var tokenManager: JWTTokenManager

    @Autowired
    lateinit var encryptorUtil: EncryptorUtil

    @Autowired
    lateinit var userRepository: UserRepository

    fun login(userName: String, password: String): LoginResponse {
        val user = userRepository.findByUSer(userName) ?: throw BusinessException("userName or password invalid")

        if (user.password != encryptorUtil.encrypt(password)) {
            throw BusinessException("userName or password invalid")
        }

        val token = tokenManager.createToken(user)

        return LoginResponse(token)
    }
}