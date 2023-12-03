package com.example.springstudy.mvc.transactional.parallel.entity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PTMemberRepository : JpaRepository<PTMember, Long> {
}
