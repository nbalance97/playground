package com.example.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test

class ScopeTest {

    @Test
    fun `GlobalScope 테스트`() {
        /**
         * GlobalScope는 내부로 들어가서 보면 사실상 EmptyCoroutineContext이다
         * 이로 인해 async에서 예외가 발생한다 하더라도 부모 코루틴이 취소되거나 하지 않는다
         */
        runBlocking {
            // deferredHello에서 예외가 발생했어야 하지만, GlobalScope는 부모 코루틴이 명시되어 있지 않으므로 전체 취소되지 않음
            val deferredHello = GlobalScope.async {
                throw IllegalArgumentException("에러 발생A")
                "hello"
            }

            try {
                deferredHello.await()
            } catch (e: Exception) {
                println("예외 발생: ${e.message}")
            }

            // 위에서 에러가 발생한것과 상관 없이, 아래 코드는 정상적으로 실행
            val deferredWorld = GlobalScope.async {
                "world"
            }

            println(deferredWorld.await())
        }
    }

    @Test
    fun `coroutineScope 테스트`() {
        /**
         * CoroutineScope의 경우 자식 코루틴들이 모두 끝나기를 기다린다
         * 따라서 A 실행 -> B 실행 -> 전체 종료 순으로 출력됨
         */
        suspend fun f() = coroutineScope {
            launch {
                delay(1000)
                println("A 실행")
            }

            launch {
                delay(2000)
                println("B 실행")
            }
        }

        runBlocking {
            f()
            println("전체 종료")
        }
    }

    @Test
    fun `coroutineScope 예외처리 테스트`() {
        suspend fun f() = coroutineScope {
            val hello = async {
                delay(1000)
                "hello"
            }

            val world = async {
                delay(2000)
                throw IllegalArgumentException("month 조회 시 에러 발생")
                "world"
            }

            "${hello.await()} - ${world.await()}"
        }

        runBlocking {
            try {
                // CoroutineScope를 try-catch로 묶어서 예외 처리가 가능하다 (scope 내의 예외를 부모가 자식을 모두 종료시키고 rethrow)
                f()
            } catch (e: Exception) {
                println("예외 발생: ${e.message}")
            }
        }
    }

    @Test
    fun `withContext 테스트`() {
        runBlocking(CoroutineName("hello worldA")) {
            withContext(CoroutineName("hello worldB")) {
                // 자식의 Context가 부모의 Context를 덮어 씌운다 (오버라이딩 된다)
                println("child coroutine Name -> ${coroutineContext[CoroutineName]?.name}")
            }
            println("parent coroutine Name -> ${coroutineContext[CoroutineName]?.name}")
        }
    }

    @Test
    fun `supervisorScope 예외처리 테스트`() {
        fun load(index: Int): String {
            if (index == 5) {
                throw IllegalArgumentException("ERROR")
            }

            return index.toString()
        }

        runBlocking {

            // supervisorScope != withContext(SuperVisorJob()) 임에 유의 (다르다 다르다 다르다)
            supervisorScope {
                val data = (1..10).map {
                    async { load(it) }
                }.mapNotNull {
                    // supervisorScope의 경우 await() 시점에 try-catch가 필요하다 (자식 코루틴들이 실패한 코루틴도 있고, 성공한 코루틴도 있으므로)
                    try {
                        it.await()
                    } catch (e: Exception) {
                        println("에러 발생: ${e.message}")
                        null
                    }
                }

                println(data)
            }
        }
    }

    @Test
    fun `withTimeout 테스트`() {
        runBlocking {
            try {
                coroutineScope {
                    withTimeout(2000) {
                        delay(1000)
                        println("hello")

                        // 여기 시점에서 TimeoutCancellationException 발생
                        delay(3000)
                        println("world")
                    }
                }
            } catch (e: TimeoutCancellationException) {
                println("예외 발생: ${e.message}")
            }
        }
    }
}
