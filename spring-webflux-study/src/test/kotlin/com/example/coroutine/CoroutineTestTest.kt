package com.example.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class CoroutineTestTest {

    @Test
    fun test() = runTest {
        println("Thread: ${Thread.currentThread().name}")

        println("time -> $currentTime")
        delay(1000)
        println("time -> $currentTime")
    }
}
