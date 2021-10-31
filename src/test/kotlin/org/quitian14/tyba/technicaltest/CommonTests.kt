package org.quitian14.tyba.technicaltest
import com.fasterxml.jackson.databind.ObjectMapper
import org.quitian14.tyba.technicaltest.services.SecurityService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
abstract class CommonTests {

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var securityService: SecurityService

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate
}
