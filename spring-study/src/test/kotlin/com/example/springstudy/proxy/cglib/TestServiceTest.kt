package com.example.springstudy.proxy.cglib

import org.junit.jupiter.api.Test

class TestServiceTest {

    val testServiceUsage: TestServiceUsage = TestServiceUsage()

    @Test
    fun cglib_프록시_테스트() {
        testServiceUsage.test()
    }
}
