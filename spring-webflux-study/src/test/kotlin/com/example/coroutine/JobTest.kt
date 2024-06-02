package com.example.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JobTest {

    @Test
    fun `Job의 Status를 확인한다`() {
        val job = Job()
        assertThat(job.isActive).isTrue() // Job Builder로 만들어진 Job은 Active 상태

        // job이 취소되거나 예외가 발생하는 상황
        job.cancel()
        assertThat(job.isActive).isFalse()
        assertThat(job.isCancelled).isTrue()

        // job이 완료되는 상황
        val anotherJob = Job()
        anotherJob.complete()
        assertThat(anotherJob.isCompleted).isTrue()
    }

    @Test
    fun `Coroutine을 직접 실행해보면서 Job의 상태를 확인한다`() {
        runBlocking {
            val job = launch {
                println("Hello world!")
            } // launch 함수는 Job을 반환한다

            // 일반적인 경우는 코루틴이 실행중이므로 Active가 나옴(컴퓨터 성능/환경에 따라 달라질 수도 있을까?)
            println("current Job -> ${job}")

            delay(1000)

            // Coroutine이 완전히 종료되고 job의 상태는 Completed
            println("current Job -> ${job}")
        }
    }

    @Test
    fun `Job Children 테스트`() {
        runBlocking {
            launch {
                delay(1000)
            }

            launch {
                delay(1000)
                println("job의 parent -> ${this.coroutineContext.job.parent}")
            }

            // runBlocking CoroutineScope의 job은 위 launch 두개가 있으니 두명이 나와야 함
            val job = coroutineContext.job
            println(job.children.toList())

            delay(2000)

            // job.children의 모든 요소가 삭제된다 (completed로 남아있는것 아님, children엔 모두 제거)
            println(job.children.toList())
        }
    }

    @Test
    fun `Job Factory 무한루프`() {
        runBlocking {
            val job = Job()

            launch (job) {
                delay(500)
                println("job1 execute")
            }

            launch (job) {
                delay(1000)
                println("job2 execute")
            }

            // 문제상황, join()을 job에 대해 걸어주면 무한루프에 걸린다
            job.join()

            // solution 1
//            println(job.children.toList())
//            job.children.forEach { it.join() }
//            println(job.children.toList()) // 남아있는 자식들이 존재하지 않는다

            // solution 2
//            job.complete()
//            job.join()
        }
    }

    @Test
    fun `Job Factory 테스트(Complete)`() {
        runBlocking {
            val job = Job()

            launch (job) {
                println("job1 execute")
                job.complete() // job 완료처리
            }

            launch (job) {
                delay(1000)
                println("job2 execute")
            }

            job.join()
        }
    }

    @Test
    fun `Job Factory 테스트(Exceptionally)`() {
        runBlocking {
            val job = Job()

            launch (job) {
                println("job1 execute")
                job.completeExceptionally(IllegalArgumentException("error")) // job 취소처리
            }

            launch (job) {
                delay(1000)
                println("job2 execute")
            }

            // completeExceptionally는 자식 코루틴들을 취소시켜 버림 (끝까지 대기하지 않으므로 job2는 실행되지 않음)
            job.join()
        }
    }

    @Test
    fun `가장 일반적인 Job 사용법`() = runTest {
        val job = Job()

        launch (job) {
            delay(500)
            println("job1 execute")
        }

        launch(job) {
            delay(300)
            println("job2 execute")
        }

        job.complete()
        job.join()
    }

    @Test
    fun `Job Test`() = runTest{
    }
}
