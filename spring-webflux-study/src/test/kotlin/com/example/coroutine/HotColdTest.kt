package com.example.coroutine

import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class HotColdTest {

    @Test
    fun `hot cold simple test`() = runTest {
        val channel = produce {
            println("값 생성 (Channel)")
            send(1)
        }

        delay(100)
        channel.cancel()

        val flow = flow<Int> {
            println("값 생성 (flow)")
            emit(1)
        }

        // 아래 코드를 주석하면 값 생성(flow)라는 문구는 출력되지 않는다
        // flow.collect { }
    }
}
