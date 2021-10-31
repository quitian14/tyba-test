package org.quitian14.tyba.technicaltest.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.quitian14.tyba.technicaltest.model.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * Class to manage JWT code and decode token
 */
@Component
class JWTTokenManager {

    @Value("\${jwt.key}")
    lateinit var jwtKey: String

    @Autowired
    lateinit var objectMapper: ObjectMapper

    fun validateToken(token: String): User {
        try {
            val algorithm = Algorithm.HMAC256(jwtKey)
            val verifier: JWTVerifier = JWT.require(algorithm)
                .withIssuer("auth0")
                .build()
            val jwt: DecodedJWT = verifier.verify(token)
            val userJson = jwt.getClaim("user")
            return objectMapper.readValue(userJson.asString(), User::class.java)
        } catch (exception: JWTVerificationException) {
            throw SecurityException("Invalid token")
        }
    }

    fun createToken(user: User): String {
        val userJson = objectMapper.writeValueAsString(user)
        val payload = mapOf<String,Any>(
            "user" to userJson,
        )

        try {
            val algorithm: Algorithm = Algorithm.HMAC256(jwtKey)
            val token = JWT.create()
                .withIssuer("auth0")
                .withPayload(payload)
                .sign(algorithm)

            return token
        } catch (exception: JWTCreationException) {
            throw SecurityException("Error building the token")
        }
    }
}
