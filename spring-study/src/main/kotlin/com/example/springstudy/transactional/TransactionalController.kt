package com.example.springstudy.transactional

import com.example.springstudy.transactional.parallel.PTMemberService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TransactionalController(
    private val ptMemberService: PTMemberService
) {

    @GetMapping("/test/parallel-transactional")
    fun parallelTransactionalTest() {
        ptMemberService.execution()
    }

    @GetMapping("/test/size")
    fun getSize() {
        ptMemberService.getSize()
    }
}
