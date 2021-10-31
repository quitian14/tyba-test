package org.quitian14.tyba.technicaltest.controllers

import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.quitian14.tyba.technicaltest.CommonTests
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class UserControllerTest: CommonTests() {

    @Test
    @Sql("/queries/loginOkTest.sql")
    fun loginOKTest(){
        val body = """
                {
                    "user": "claudia_1234",
                    "password": "Clao1234#"
                }
            """.trimIndent()

        val request = MockMvcRequestBuilders.post("${Routes.USER}/${Routes.LOGIN}")
            .content(body)
            .contentType(MediaType.APPLICATION_JSON)

        val response = mockMvc.perform(request)
        response.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.token", Matchers.isA<String>(String::class.java)))

    }

    @Test
    @Sql("/queries/loginOkTest.sql")
    fun loginWrongPassword(){
        val body = """
                {
                    "user": "claudia_1234",
                    "password": "Clao1234"
                }
            """.trimIndent()

        val request = MockMvcRequestBuilders.post("${Routes.USER}/${Routes.LOGIN}")
            .content(body)
            .contentType(MediaType.APPLICATION_JSON)

        val response = mockMvc.perform(request)
        response.andExpect(MockMvcResultMatchers.status().isPreconditionFailed)
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.`is`("userName or password invalid")))

    }

    @Test
    @Sql("/queries/loginOkTest.sql")
    fun loginWrongUSer(){
        val body = """
                {
                    "user": "claudia_123",
                    "password": "Clao1234#"
                }
            """.trimIndent()

        val request = MockMvcRequestBuilders.post("${Routes.USER}/${Routes.LOGIN}")
            .content(body)
            .contentType(MediaType.APPLICATION_JSON)

        val response = mockMvc.perform(request)
        response.andExpect(MockMvcResultMatchers.status().isPreconditionFailed)
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.`is`("userName or password invalid")))
    }

    @Test
    @Sql("/queries/createUserOk.sql")
    fun createUser() {
        val body = """
            {
                "user_name": "coco1234",
                "password": "Coco1234#",
                "mail": "coco@claudia.com",
                "name": "coco perro"
            }
        """.trimIndent()
        val token = securityService.login("claudia_1234", "Clao1234#")

        val request = MockMvcRequestBuilders.post("${Routes.USER}")
            .content(body)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer ${token.token}")

        val response = mockMvc.perform(request)
        response.andExpect(MockMvcResultMatchers.status().isOk)

    }

    @Test
    @Sql("/queries/createUserOk.sql")
    fun createUserBadPermission() {
        val body = """
            {
                "user_name": "coco1234",
                "password": "Coco1234#",
                "mail": "coco@claudia.com",
                "name": "coco perro"
            }
        """.trimIndent()
        val token = securityService.login("claudia_1234", "Clao1234#")

        val request = MockMvcRequestBuilders.post("${Routes.USER}")
            .content(body)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer ${token.token}")

        val response = mockMvc.perform(request)
        response.andExpect(MockMvcResultMatchers.status().isOk)
    }
}