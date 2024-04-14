package com.example.springstudy.proxy

import com.example.springstudy.proxy.postprocessor.PostProcessorTest
import org.junit.jupiter.api.Test


class PostProcessorTests {

    @Test
    fun test() {
        val target = PostProcessorTest()

        target.test()
    }
}
