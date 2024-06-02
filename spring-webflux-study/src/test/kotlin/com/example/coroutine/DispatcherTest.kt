package com.example.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.lang.Thread.sleep

class DispatcherTest {

    @Test
    fun `Dispatcher 쓰레드명 테스트`() = runTest {
        println("Main: ${Thread.currentThread().name}")

        repeat(10) {
            /**
             * Dispatchers.Default는 CPU 개수와 동일한 수의 스레드 풀을 가지고 있음 (최소 두개)
             */
            launch(Dispatchers.Default) {
                println("Default: ${Thread.currentThread().name}")
            }
        }
    }

    @Test
    fun `test`() {
        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {

        }
    }

    @Test
    fun `runBlocking 쓰레드명 테스트`() {
        /**
         * runBlocking의 경우 자신만의 디스패처를 사용하여 코루틴을 실행
         * 따라서 runBlocking 내의 모든 코루틴은 동일한 스레드에서 실행되는 것으로 보임
         */
        runBlocking {
            repeat(10) {
                launch {
                    println("thread ->: ${Thread.currentThread().name}")
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Dispatchers 최대 쓰레드 수 제한하는 케이스`() = runTest {
        val dispatcher = Dispatchers.Default
            .limitedParallelism(3) // 최대 쓰레드 수가 3개

        repeat(100) {
            launch(dispatcher) {
                println("Default: ${Thread.currentThread().name}")
            }
        }
    }

    @Test
    fun `Dispatcher IO 테스트`() = runTest {
        /**
         * Dispatchers.IO의 쓰레드 총 개수는 64개 (CPU 코어 수가 64개보다 많다면 코어 수로)
         */
        repeat(100) {
            // 64개가 한번에 처리되고, 나머지 36개가 다음 순서에 처리되므로 대략 2초정도 걸림
            launch(Dispatchers.IO) {
                Thread.sleep(1000) // Thread를 블로킹하는 케이스
                println("${it} 작업 완료")
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Dispatcher IO 테스트 - 병렬이 제어된 Dispatcher`() = runTest {
        repeat(100) {
            // 한번에 최대 100개의 작업을 처리할수 있도록 커스텀 하였으므로 1초만에 끝나버림
            // Dispatchers.IO.limitedParallelism -> Dispatchers.IO와 무관한 독립적인 디스패처 생성
            val dispatcher = Dispatchers.IO.limitedParallelism(100)
            launch(dispatcher) {
                Thread.sleep(1000)
                println("${it} 작업 완료")
            }
        }
    }

    @Test
    fun `Dispatchers 테스트 - Single Thread`() = runTest {
        val dispatcher = Dispatchers.IO.limitedParallelism(1)

        listOf(1, 2, 3).map { async { it } }.awaitAll()

        repeat(5) {
            launch(dispatcher) {
                println("thread: ${Thread.currentThread().name}")
            }
        }
    }

    @Test
    fun `Dispatcher ThreadPool 테스트`() = runTest {
        var threadAName = ""
        var threadBName = ""

        launch(Dispatchers.Default) {
            sleep(1000)
            threadAName = Thread.currentThread().name
        }

        launch(Dispatchers.IO) {
            sleep(1000)
            threadBName = Thread.currentThread().name
        }

        coroutineContext.job.children.forEach { it.join() }

        /**
         * Dispatchers.Default와 Dispatchers.IO는 공유 쓰레드풀을 사용하기 때문에 모두 DefaultDispatcher-worker-n이라는 쓰레드가 나옴
         */
        println("threadAName: $threadAName")
        println("threadBName: $threadBName")

        /**
         * 공유 쓰레드풀을 사용하지 않도록 하려면 어떻게 하면 될까?
         */
        val dispatcher = newFixedThreadPoolContext(2, "myPool")
        launch(dispatcher) {
            println("thread: ${Thread.currentThread().name}")
        }
    }
}
