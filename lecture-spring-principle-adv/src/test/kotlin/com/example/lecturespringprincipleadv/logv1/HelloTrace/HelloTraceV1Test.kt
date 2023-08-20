package com.example.lecturespringprincipleadv.logv1.HelloTrace

import org.junit.jupiter.api.Test

class HelloTraceV1Test {

    @Test
    fun begin() {
        val helloTraceV1 = HelloTraceV1()

        val status = helloTraceV1.begin("hello")
        helloTraceV1.end(status)
    }
}
