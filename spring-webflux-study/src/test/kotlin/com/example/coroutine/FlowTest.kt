package com.example.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.asFlux
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import java.lang.Thread.sleep
import kotlin.random.Random

class FlowTest {

    val logger = LoggerFactory.getLogger(javaClass)

    @Test
    fun `SimpleFlowTest`() = runBlocking(Dispatchers.Default) {
//        sequence {
//            for (i in 1..10) {
//                sleep(100)
//                yield(i)
//            }
//        }.forEach { println(it) }

        // flow 개정판 (성능 변화는 없음)
        flow {
            for (i in 1..10) {
                delay(100)
                emit(i)
            }
        }.collect { logger.info("출력 데이터 -> ${it}") }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Flow 동시성 호출 제어 Test`() = runBlocking(Dispatchers.Default) {
        (0..100).asFlow()
            .flatMapMerge(concurrency = 5) {
                flow {
                    // Blocking Call이 있는 경우
//                    sleep(100)

                    delay(1000)
                    logger.info("처리 데이터 -> ${it}")
                    emit(it)
                }
            }
            .collect {
                logger.info("출력 데이터 -> ${it}")
            }
    }

    @Test
    fun `Flow 동시성 테스트(내부)`() = runBlocking(Dispatchers.Default) {
        fun Flow<*>.f(): Flow<*> = flow {
            val mutableList = mutableListOf<Int>()

            collect {
                mutableList.add(it as Int)
                emit(mutableList.sum())
            }
        }

        (0..10).asFlow()
            .f()
            .collect {
                logger.info("출력 데이터 -> ${it}")
            }
    }

    @Test
    fun `Flow 동시성 테스트(내부)2`(): Unit = runBlocking(Dispatchers.Default) {
        fun Flow<Int>.f(): Flow<Int> = flow {
            val mutableList = mutableListOf<Int>()

            collect {
                mutableList.add(it)
                sleep(10)
                emit(mutableList.sum())
            }
        }

        val flow = (0..10).asFlow().f()

        launch {
            logger.info(flow.last().toString())
        }

        launch {
            logger.info(flow.last().toString())
        }
    }

    @Test
    fun `Flow 동시성 테스트(외부)`(): Unit = runBlocking(Dispatchers.Default) {

        fun Flow<*>.counter(): Flow<Int> {
            var counter = 0

            return this.map {
                counter++
                List(100) { Random.nextLong() }.shuffled().sorted()
                counter
            }
        }

        val f1 = List(1_000) { Random.nextLong() }.asFlow()
        val f2 = List(1_000) { Random.nextLong() }.asFlow()
            .counter()

        launch { println(f1.counter().last()) }
        launch { println(f1.counter().last()) }

        // 추론 : 같은 flow를 사용하는 경우에 counter가 공유되니 문제가 있을 수 있겠다
        launch { println(f2.last()) }
        launch { println(f2.last()) }
    }

    @Test
    fun `Flow 동시성 테스트(외부)2`(): Unit = runBlocking(Dispatchers.Default) {

        fun Flow<Int>.f2(): Flow<Int> {
            val mutableList: MutableList<Int> = mutableListOf()
            val lock: Any = Any()

            return this.map {
                // mutableList는 동시에 add하는 경우 ConcurrentModificationException
                synchronized(lock) {
                    mutableList.add(it)
                }

                // 동시성 이슈를 만들기 위한 sleep
                sleep(10)

                mutableList.sum()
            }
        }

        val f3 = List(1_0) { 1 }.asFlow()
            .f2()

        // 둘다 10을 예상하지만 둘다 19가 나와버림
        launch { println(f3.last()) }
        launch { println(f3.last()) }
    }

    @Test
    fun `Flow 생성 종합 테스트`(): Unit = runBlocking {
        flowOf(1, 2, 3, 4, 5)
            .collect { println(it) }
        println("---")

        // 값이 없는 플로우 (값이 없으므로 println에 아무것도 안나옴)
        emptyFlow<Int>()
            .collect { println(it) }

        // 컨버터(Iterable, Iterator, Sequence -> flow)
        (1..5).asFlow()
            .collect { println(it) }
        println("---")

        // 함수 -> flow
        // 이 때 함수는 () -> T 또는 suspend () -> T 형태여야 함
        val func = suspend {
            "abcde"
        }

        func.asFlow()
            .collect { println(it) }
        println("---")

        // reactor -> flow (flowable, observable도 가능)
        Flux.just(1, 2, 3, 4, 5)
            .asFlow()
            .collect { println(it) }
        println("---")

        // 반대도 가능
        flowOf(1, 2, 3, 4, 5)
            .asFlux()
            .doOnNext { println(it) }
            .subscribe()
    }

    @Test
    fun `flow builder test`(): Unit = runBlocking(Dispatchers.Default) {
        flow {
            emit(1)
            emit(2)
            emit(3)
        }.collect {
            logger.info("$it")
        }
    }

    @Test
    fun `channelFlow 테스트`(): Unit = runBlocking(Dispatchers.Default) {
        channelFlow {
            for (i in 1..10) {
                logger.info("$i 생성")
                send(i)
            }
        }.collect {
            logger.info("$it")
        }
    }
}
