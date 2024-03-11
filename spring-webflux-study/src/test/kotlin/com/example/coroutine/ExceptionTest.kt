package com.example.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ExceptionTest {

    @Test
    fun `exception test in wrong case`() {
        /**
         * 예외처리 (잘못된 케이스)
         */
        runBlocking {
            try {
                launch {
                    throw IllegalArgumentException("error")
                }
            } catch (e: Exception) {
                println("히히 여기서 예외가 잡히겠지?") // 안잡힘. 에러 뜬 상태로 끝나버림
            }

            launch {
                delay(2000)
                // 2000ms 이내에 위 launch {} 블록에서 에러가 발생하여 부모에 전파되고, 부모는 자식 코루틴을 모두 취소시킨다
                println("do not printed")
            }
        }
    }

    @Test
    fun `SupervisorJob을 갖는 새 Scope를 만든다`() {
        runBlocking {
            val scope = CoroutineScope(SupervisorJob())

            scope.launch {
                throw IllegalArgumentException("error")
            }

            scope.launch {
                delay(2000)
                // 2000ms 이내에 위 launch {} 블록에서 에러가 발생하여 부모에 전파되고, 부모는 자식 코루틴을 모두 취소시킨다
                println("do not printed")
            }

            scope.coroutineContext.job.children.forEach { it.join() }
        }
    }

    @Test
    fun `supervisorJob을 launch로 넘겨서 동작`() {
        runBlocking {
            val job = SupervisorJob()

            launch(job) {
                throw IllegalArgumentException("error")
            }

            launch(job) {
                delay(2000)
                println("printed")
            }

            job.children.forEach { it.join() }
        }
    }

    @Test
    fun `withContext(supervisorJob) 테스트`() {
        runBlocking {

            // withContext가 사실상 자식 코루틴의 부모이고 SupervisorJob은 withContext의 부모
            // 이로 인해 SupervisorJob을 써주는것과 상관없이 자식 코루틴은 항상 실패한다
            withContext(SupervisorJob()) {
                launch {
                    throw IllegalArgumentException("error")
                }

                launch {
                    delay(2000)
                    println("printed")
                }
            }
        }
    }

    @Test
    fun `supervisorScope 테스트`() {
        runBlocking {
            // supervisorScope를 써주면 자식 코루틴이 실패하더라도, 다른 자식 코루틴을 실패시키지 않는다
            // withContext(SupervisorJob())와 같은 효과가 아님에 유의
            supervisorScope {
                launch() {
                    throw IllegalArgumentException("error")
                }

                launch() {
                    delay(2000)
                    println("printed")
                }
            }
        }
    }

    @Test
    fun `async-await 예외처리 테스트`() {
        runBlocking {
            supervisorScope {
                /**
                 * async-await의 경우 await() 하는 시점에 try-catch로 예외를 처리할수 있음
                 */
                val deferred = async {
                    throw IllegalArgumentException("에러가 발생하였습니다.")

                    "abc"
                }

                /**
                 * 아래처럼 예외를 잡아서 처리할수 있음
                 * try {
                 *    deferred.await()
                 * } catch (e: Exception) {
                 *    ...
                 * }
                 */

                assertThrows<IllegalArgumentException>("에러가 발생하였습니다.") {
                    deferred.await()
                }
            }
        }
    }

    @Test
    fun `invokeOnCompletion에 핸들러를 달아서 예외처리`() {
        assertThrows<IllegalArgumentException> {
            runBlocking {
                val currentScopeJob = coroutineContext.job
                currentScopeJob.invokeOnCompletion { cause ->
                    if (cause != null) {
                        println("job이 예외와 함께 종료되었습니다")
                    }

                    println("job이 종료되었습니다")
                }

                launch {
                    throw IllegalArgumentException("error")
                }
            }
        }
    }

    @Test
    fun test() {
        runBlocking {
            val handler = CoroutineExceptionHandler { _, exception ->
                println("CoroutineExceptionHandler got ${exception}")
            }

            val scope = CoroutineScope(SupervisorJob() + handler)
            scope.launch {
                throw IllegalArgumentException("error")
            }
        }
    }
}
