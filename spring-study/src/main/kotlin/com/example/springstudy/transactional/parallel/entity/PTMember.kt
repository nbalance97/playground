package com.example.springstudy.transactional.parallel.entity

import jakarta.persistence.*

@Entity
class PTMember(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String = "NoName",
) {
}
