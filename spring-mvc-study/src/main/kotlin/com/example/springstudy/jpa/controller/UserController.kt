package com.example.springstudy.jpa.controller

import com.example.springstudy.jpa.dto.UserResponse
import com.example.springstudy.jpa.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) {

    @GetMapping("/user")
    fun getUser(@RequestParam id: String?): UserResponse = userService.findById(id)
}
