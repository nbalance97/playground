package com.example.coroutine

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test


fun interface FlowCollector2<T> {
    suspend fun emit(value: T)
}

class Flow1ImplFinal {

    interface Flow2<T> {
        suspend fun collect(collector: FlowCollector2<T>)
    }

    fun <T> flow2(builder: suspend FlowCollector2<T>.() -> Unit): Flow2<T> {

        return object : Flow2<T> {
            override suspend fun collect(collector: FlowCollector2<T>) {
                collector.builder()
            }
        }
    }

    @Test
    fun `수제제작 flow`() = runTest {
        flow2 {
            // 여기서 emit()을 호출하는데, 이 emit이 아래 collector의 FlowCollector#emit이다
            // collect의 lambda가 emit이라고 보아도 됨
            emit("Hello")
        }
            .collect {
                println(it)
            }
    }

    // Map 구현 테스트
    fun <T, R> Flow2<T>.map2(transform: suspend (value: T) -> R): Flow2<R> {
        return flow2 {
            collect {
                println(it)
                emit(transform(it))
            }
        }
    }

    @Test
    fun `수제제작 flow with map`() = runTest {
        flow2 {
            emit("Hello")
        }
            .map2 { "$it World" }
            .collect {
                println(it)
            }
    }
}
