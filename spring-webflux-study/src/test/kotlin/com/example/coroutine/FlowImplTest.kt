package com.example.coroutine

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test


fun interface FlowCollector1 {
    suspend fun emit(value: String)
}

class Flow1ImplTest {

    @Test
    fun `lambda`() = runTest {
        val f: () -> Unit = {
            println("Hello")
        }

        f()

        // 람다식은 중단함수로도 선언할 수 있다
        val suspendF: suspend () -> Unit = {
            println("Hello")
        }

        suspendF()

        // 위 예시에서 파라메터에도 함수를 받을수 있다
        val suspendF2: suspend ((String) -> Unit) -> Unit = { emit ->
            emit("Hello")
        }

        suspendF2 { println(it) }

        // 위에서 (String -> Unit)을 함수형 인터페이스로 받아보자
        val suspendF3: suspend (FlowCollector1) -> Unit = { it.emit("Hello") }

        suspendF3 { println(it) }

        // it도 쓰기 귀찮다
        val suspendF4: suspend FlowCollector1.() -> Unit = {
            emit("Hello") }

        suspendF4 { println(it) }
    }


    interface Flow1 {

        suspend fun collect(collector: FlowCollector1)
    }

    @Test
    fun `flow 만들기 2`() = runTest {
        val flow1 = object : Flow1 {
            override suspend fun collect(collector: FlowCollector1) {
                collector.emit("Hello")
            }
        }

        flow1.collect { println(it) }

        // 위 코드의 flow를 빌더로 만들어 보자
        fun flow(block: suspend FlowCollector1.() -> Unit): Flow1 {
            return object : Flow1 {
                override suspend fun collect(collector: FlowCollector1) {
                    collector.block()
                }
            }
        }

        flow {
            emit("Hello")
        }
            .collect { println(it) }
    }
}
