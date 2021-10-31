package org.quitian14.tyba.technicaltest.repositories

import org.quitian14.tyba.technicaltest.model.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class UserRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    fun findByUSer(userName: String): User? {
        val result = jdbcTemplate.queryForList(
            "SELECT u.user_name, u.password, u.mail, u.name," +
                "p.name as permission FROM users u " +
                "LEFT JOIN user_permissions up ON (u.user_name = up.user_name) " +
                "LEFT JOIN permissions p ON (p.id = up.permission_id) " +
                "WHERE u.user_name = ?", userName
        )

        if (result.isEmpty()) return null

        val user = User(result[0]["user_name"].toString(),
            result[0]["mail"].toString(),
            result[0]["name"].toString(),
            result[0]["password"].toString()
        )

        val permissions = result.map { it["permission"] as String? }
        user.permissions = permissions.filterNotNull()

        return user
    }

    fun createUser(user: User) {
        jdbcTemplate.update("INSERT INTO users(user_name, password, mail, name) " +
            "VALUES (?,?,?,?)", user.userName, user.password, user.mail, user.name)
    }
}