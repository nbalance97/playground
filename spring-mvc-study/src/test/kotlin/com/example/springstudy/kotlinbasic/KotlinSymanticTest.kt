package com.example.springstudy.kotlinbasic

import org.apache.coyote.http11.Constants.a
import org.junit.jupiter.api.Test

class KotlinSymanticTest {

    @Test
    fun test() {
        a in 1..9
        // 식에서의 in은 contains == 1 <= a <= 9

        for (i in '1'..'9') {}
        // for문에서의 in은 iterator ( 1~9까지의 range loop)

        val hasText = "a" in "abc".."xyz" // no error
        // for (i in "abc".."xyz") {} // error, String은 range loop가 안됨
    }
}
