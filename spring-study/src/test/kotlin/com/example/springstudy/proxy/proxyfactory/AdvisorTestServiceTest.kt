package com.example.springstudy.proxy.proxyfactory

import org.junit.jupiter.api.Test

class AdvisorTestServiceTest {

    @Test
    fun advisor_테스트() {
        AdvisorTestService().test()
    }

    @Test
    fun aspectJPointcut_테스트() {
        AdvisorTestService().aspectJPointcutTest()
    }

    @Test
    fun aspectJPointcut_잘못된_포인트컷() {
        AdvisorTestService().aspectJPointcutTestWithWrongPointcut()
    }
}
