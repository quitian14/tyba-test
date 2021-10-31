package org.quitian14.tyba.technicaltest.services

import org.quitian14.tyba.technicaltest.model.dtos.ParamDto
import org.quitian14.tyba.technicaltest.model.dtos.RestaurantDto
import org.quitian14.tyba.technicaltest.model.entities.UserTransaction
import org.quitian14.tyba.technicaltest.repositories.UserTransactionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserTransactionService {

    @Autowired
    lateinit var userTransactionRepository: UserTransactionRepository

    fun create(userName: String, params: ParamDto?, restaurantsDto: List<RestaurantDto>) {
        val userTransaction = UserTransaction(userName = userName, params = params, restaurants = restaurantsDto)
        userTransactionRepository.createUserTransaction(userTransaction)
    }

    fun findByUser(userName: String): List<UserTransaction> {
        return userTransactionRepository.findByUSer(userName)
    }
}