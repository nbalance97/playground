package com.example.coroutine

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class FlowLifeCycleTest {

    val logger = LoggerFactory.getLogger(javaClass)

    @Test
    fun `eventTest`() = runTest {
        flowOf(1, 2, 3, 4, 5)
            .onStart {
                // onStart에서도 flow에 원소를 흘려내려보낼수 있음
                // 이 때 start에서 보낸 원소가 먼저 내려간다
                logger.info("#doOnStart")
                emit(0)
            }
            .onEach { logger.info("#doOnNext: $it") }
            .onCompletion { logger.info("#doOnComplete") }
            .collect { element -> logger.info("element: $element") }
    }

    @Test
    fun `onEmptyTest`() = runTest {
        emptyFlow<Int>()
            .onStart { logger.info("#doOnStart") }
            .onCompletion { logger.info("#doOnComplete") }
            .onEmpty { logger.info("#doOnEmpty") }
            .onEach { logger.info("#doOnNext: $it") }
            .collect { element -> logger.info("element: $element") }
    }

    @Test
    fun `onCatch 테스트`() = runTest {
        flow {
            emit(0)
            emit(1)
            throw IllegalArgumentException("Error")
            emit(2)
        }
            // catch에서도 마찬가지로 emit()으로 원소를 내려보낼 수 있음
            .catch { exception -> logger.error("Error: $exception") }
            .collect { element -> logger.info("element: $element") }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `flow 테스트`() = runTest {
        val flow = flowOf(flowOf(1,2,3), flowOf(4,5,6), flowOf(7,8,9))
    }
}
