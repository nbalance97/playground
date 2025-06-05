package com.example.springstudy.jpa.entity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long>


fun <T, ID> JpaRepository<T, ID>.findByIdOrNull(id: ID): T? = findById(id!!).orElse(null)
// @TODO: id!!로 선언해야 하는 이유 찾아보기
