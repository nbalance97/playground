package com.example.springstudy.jpa.controller

import com.example.springstudy.jpa.dto.UserResponse
import com.example.springstudy.jpa.service.UserService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(UserController::class)
class UserControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    // @MockBean , mockbean은 deprecated
    @MockitoBean
    lateinit var userService: UserService

    @Test
    fun `getUser should return user response`() {
        // given
        val userId = "1"
        val userResponse = UserResponse(id = userId, name = "테스트유저", grade = "ABC")
        given(userService.findById(userId)).willReturn(userResponse)

        // when & then
        mockMvc.get("/user") {
            param("id", userId)
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.id") { value(userId) }
                jsonPath("$.name") { value("테스트유저") }
            }
    }
}

