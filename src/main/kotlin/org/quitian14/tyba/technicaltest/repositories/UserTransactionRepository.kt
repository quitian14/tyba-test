package org.quitian14.tyba.technicaltest.repositories

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.quitian14.tyba.technicaltest.model.dtos.ParamDto
import org.quitian14.tyba.technicaltest.model.dtos.RestaurantDto
import org.quitian14.tyba.technicaltest.model.entities.UserTransaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.Date

@Component
@Transactional
class UserTransactionRepository {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    fun findByUSer(userName: String): List<UserTransaction> {
        val result = jdbcTemplate.queryForList(
            "SELECT u.id, u.user_name, u.params, u.restaurants, u.created_at FROM user_transactions u " +
                " WHERE u.user_name = ?", userName
        )

        if (result.isEmpty()) return listOf()

        return result.map {
            UserTransaction(
                it["id"] as Long,
                it["user_name"].toString(),
                objectMapper.readValue(it["params"].toString(), ParamDto::class.java),
                objectMapper.registerModule(JavaTimeModule()).readValue(
                    it["restaurants"].toString(),
                    object : TypeReference<List<RestaurantDto>>() {}
                ),
                it["created_at"] as Date?
            )
        }
    }

    fun createUserTransaction(userTransaction: UserTransaction) {
        jdbcTemplate.update(
            "INSERT INTO user_transactions(user_name, params, restaurants) " +
                "VALUES (?,?::jsonb,?::jsonb)",
            userTransaction.userName,
            objectMapper.writeValueAsString(userTransaction.params),
            objectMapper.writeValueAsString(userTransaction.restaurants)
        )
    }
}