package com.example.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class CoroutineBuilderTest {

    @Test
    fun `Coroutine Lazy Start Test`() = runTest {

        val job = launch(start = CoroutineStart.LAZY) {
            println("hello world!")
        }

        // job의 상태를 찍기 위한 로깅
        println("$job") // New
        job.start() // 지연 코루틴을 실행하기 위해서는 명시적으로 start() 메서드 호출 필요
        println("$job") // Job 실행 (Active)
        delay(1000)
        println("$job") // Job 완료 (Completed)
    }

    @Test
    fun `async-await basic`() = runTest {
        /**
         * 여러 코루틴이 함께 실행되어야 하는 경우 async-await을 사용할 수 있다
         * 주의할 점은, await은 최대한 나중에 써주어야 한다는 점이다
         * await() 이후에 async가 나오면 해당 코루틴이 완료되기 전까지 대기하게 된다
         */
        val phone = async {
            delay(1000)
            println("phone load")
            return@async "phone"
        }

        // 여기서 phone.await()을 해주면 아래 key 코루틴은 실행되지 못하고 phone 코루틴이 모두 완료될때까지 대기한다
        // phone.await()

        val key = async {
            delay(1000)
            println("key load")
            return@async "key"
        }

        val pocketsDeferred = listOf(phone, key)

        // 이 때, pocket에서 값을 가져오는 방법은 두가지
        // 자잘한 내용) 한번 완료된 코루틴을 await() 여러번 한다고 여러번 실행되는 것은 아니다
        // 1) awaitAll
        val pockets = pocketsDeferred.awaitAll()
        println(pockets)

        // 2) await
        val pockets2 = pocketsDeferred.map { it.await() }
        println(pockets2)
    }

    @Disabled
    @Test
    fun `무한 로딩되는 케이스 테스트`() = runTest {
        // CoroutineStart.LAZY 옵션을 주어서 코루틴을 생성한 경우 부모 코루틴은 자식 코루틴이 실행되기 전까지 대기한다
        // 즉, 명시적으로 start 해주지 않으면 무한정 대기하게 된다
        val phone = async(start = CoroutineStart.LAZY) {
            delay(1000)
            println("phone load")
            return@async "phone"
        }

        val key = async {
            delay(1000)
            println("key load")
            return@async "key"
        }

        val pocketsDeferred = listOf(phone, key)
//
//        val pockets = pocketsDeferred.awaitAll()
//        println(pockets)
//
//        val pockets2 = pocketsDeferred.map { it.await() }
//        println(pockets2)
    }
}
