package com.example.springstudy.proxy

import com.example.springstudy.proxy.cglib.TestServiceUsage
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller {

    private val usage = TestServiceUsage()

    @GetMapping("/test")
    fun test() {
        usage.test()
    }
}
