package com.example.coroutine

import kotlinx.coroutines.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine

@DisplayName("중단함수 테스트 - suspendCoroutine")
class SuspendCoroutineTest {

    @Test
    fun suspendCoroutine_테스트() {
        runBlocking {
            val value = suspendCoroutine {
                it.resumeWith(Result.success("hello"))
            }

            assertThat(value).isEqualTo("hello")
        }
    }

    @Disabled("resume 호출 없이 suspendCoroutine 호출 시 무한 대기")
    @Test
    fun `resume_없이_테스트한다`() {
        runBlocking {
            suspendCoroutine<Nothing> {
                //it.resumeWith(Result.success("hello"))
            }

            print("finished")
            // assertThat(value).isEqualTo("hello")
        }
    }

    @DisplayName("권장되지 않는 방식임에 유의 (메모리 누수 발생 가능)")
    @Test
    fun continuation_객체를_외부에() {
        runBlocking {
            var outerContinuation: Continuation<String>? = null

            launch {
                delay(3000)
                outerContinuation?.resumeWith(Result.success("hello"))
            }

            val continuation = suspendCoroutine<String> {
                outerContinuation = it // 외부 outerContinuation에 suspendCoroutine에서 받은 Continuation 저장
            }

            assertThat(continuation).isEqualTo("hello")
        }
    }
}
