package com.example.springstudy.jpa.service

import com.example.springstudy.jpa.dto.UserResponse
import com.example.springstudy.jpa.entity.UserRepository
import com.example.springstudy.jpa.entity.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional
    fun findById(id: String?): UserResponse {
        val user = userRepository.findByIdOrNull(id?.toLong() ?: 0L)

        return user?.let {
            UserResponse(
                id = it.id.toString(),
                name = it.name,
                grade = it.grade
            )
        } ?: throw IllegalArgumentException("User not found with id: $id")
    }
}
