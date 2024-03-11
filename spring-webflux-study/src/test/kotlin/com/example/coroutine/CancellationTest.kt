package com.example.coroutine

import kotlinx.coroutines.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CancellationTest {

    @Test
    fun `job cancellation 테스트`() {
        runBlocking {
            val job = Job()
            job.cancel(CancellationException("취소 테스트"))

            // 부모 기반으로 Job을 만든 경우, 취소된 부모 대상으로 Job을 만드는 경우 정상적으로 연결되지 않는 것을 확인
            val child = Job(job)
            println("child jobs -> ${job.children.toList()}")
            println("parent job -> ${child.parent}")
            assertThat(child.isCancelled).isTrue()

            // 일반적인 케이스
            val normalJob = Job()
            val normalChild = Job(normalJob)
            println("child jobs -> ${normalJob.children.toList()}")
            println("parent job -> ${normalChild.parent}")
            assertThat(normalChild.isActive).isTrue()
        }
    }

    @Test
    fun `cancellation_with_join`() {
        runBlocking {
            val job = launch {
                repeat(1_000) {
                    delay(100)
                    Thread.sleep(100)
                    println("job - [$it]")
                }
            }

            delay(1_000)
            job.cancel()
            job.join() // job이 마칠때까지 중단 (예제에서는 없으면 경쟁상태가 생긴다고 함)
            println("cancel successfully")
        }
    }

    @Test
    fun `cancellation_with_try_catch`() {
        runBlocking {
            val job = launch {
                try {
                    repeat(1_000) {
                        delay(100)
                        println("job - [$it]")
                    }
                } catch (e: CancellationException) {
                    println("job is cancelled")
                    throw e // exception을 다시 던지는 것이 좋다
                }
            }

            delay(1_000)
            job.cancel()

            // 잡의 상태를 출력하기 위함 (println)
            println(job) // cancelling
            job.join()
            println(job) // cancelled

            println("cancel successfully")
        }
    }

    @Test
    fun `취소된 job에서 코루틴이나 중단함수를 호출하는 케이스 (실행 안됨)`() {
        runBlocking {
            val job = launch {
                try {
                    repeat(1_000) {
                        delay(100)
                        println("job - [$it]")
                    }
                } catch (e: CancellationException) {
                    // 이 시기에 들어오면 job은 사실상 cancelling 상태

                    // 1. 취소된 job에서 코루틴이 실행되지 않는다 (무시됨)
                    launch { println("end coroutine!! hhh") }

                    // 2. 취소된 job에서 중단함수를 호출하는 경우 CancellationException 발생
                    try {
                        delay(100000)
                        println("hello delay") // 이 부분은 실행되지 않음 (delay에서 예외가 터져버리기 때문)
                    } catch (e: CancellationException) {
                        println("delay is cancelled")
                    }
                    throw e
                }
            }

            delay(1_000)
            job.cancelAndJoin()
            println("cancel successfully")
        }
    }

    @Test
    fun `취소된 Job에서 중단함수를 실행시키는 케이스 (잘 되도록)`() {
        runBlocking {
            val job = launch {
                try {
                    repeat(1_000) {
                        delay(100)
                        println("job - [$it]")
                    }
                } catch (e: CancellationException) {
                    // 이 시기에 들어오면 job은 사실상 cancelling 상태
                    // 일반적인 코루틴 빌더로는 취소된 job에서 코루틴이 실행되지 않는다 (무시됨)
                    // withContext(NonCancellable)로 감싼 경우 취소 상태인 경우에도 실행시킬수 있다 - NonCancellable : 취소시킬수 없는 Job
                    withContext(NonCancellable) {
                        println("end coroutine!! hhh")
                    }

                    throw e
                }
            }

            delay(1_000)
            job.cancelAndJoin()
            println("cancel successfully")
        }
    }

    @Test
    fun `Job이 완료된 경우 invokeOnCompletion 테스트`() {
        runBlocking(SupervisorJob()) {
            val job = launch {
                println("Job Execution")

                throw CancellationException("error")
            }

            // job이 완료된 상황에 실행 (취소된 경우에도 실행)
            job.invokeOnCompletion { cause ->
                if (cause != null) {
                    println("cause : ${cause.message}")
                }
                println("Job is completed")
            }

            delay(1_000)
            job.cancelAndJoin()
            println("cancel successfully")
        }
    }
}

