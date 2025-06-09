package com.example.springstudy.jpa.entity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long>

// 왜 !!? -> package-info에 NonNullApi가 선언되어 있기 때문
fun <T, ID> JpaRepository<T, ID>.findByIdOrNull(id: ID): T? = findById(id!!).orElse(null)
