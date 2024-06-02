package com.example.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * <p> https://kt.academy/article/cc-coroutine-context </p>
 */
class CoroutineContextTest {

    /**
     * CoroutineName의 경우 CoroutineName#Key를 키로 해서 context에 넣어둔다
     * CoroutineName#Key는 Companion Object로, 이런 구조를 많이 사용한다고 한다. (Companion Object를 키로 사용하여 대상 가져옴)
     */
    @Test
    fun CoroutineName_테스트() {
        val context: CoroutineContext = CoroutineName("hello world")
        val name = context.get(CoroutineName.Key) // == context[CoroutineName]

        assertThat(name?.name).isEqualTo("hello world")
    }

    /**
     * Job도 CoroutineContext, CoroutineName과 같은 방식으로 context에 넣어둔다
     */
    @Test
    fun CoroutineContext_합치기() {
        val context1 = CoroutineName("job1")
        val context2 = Job()
        val combined = context1 + context2

        assertThat(combined[CoroutineName]?.name).isEqualTo("job1")
        assertThat(combined[Job]).isNotNull
    }

    @Test
    fun 새_Context에_있는_key가_기존_Context의_key를_대체한다() {
        val hello = CoroutineName("hello")
        val world = CoroutineName("world")
        val combined = hello + world
        // hello name -> world name으로 대체되는 것을 볼 수 있음

        assertThat(combined[CoroutineName]?.name).isEqualTo("world")
    }

    @Test
    fun emptyCoroutineContext() {
        val context = EmptyCoroutineContext // EmptyCoroutineContext는 Object

        assertThat(context[CoroutineName]).isNull()
    }

    @Test
    fun 전체_동작_실습() {
        runBlocking {
            withContext(CoroutineName("hello")) {
                // block의 주체가 CoroutineScope이며 CoroutineScope 내에 coroutineContext가 있음
                println("withContext Coroutine Name : ${coroutineContext[CoroutineName]?.name}")

                // 자식 코루틴에서 부모 코루틴의 Context Override
                launch (CoroutineName("world")) {
                    println("launch Coroutine Name : ${coroutineContext[CoroutineName]?.name}")
                }

                // 자식 코루틴에서 부모 코루틴의 Context를 그대로 사용
                launch {
                    println("launch Coroutine Not CoroutineName : ${coroutineContext[CoroutineName]?.name}")
                    println(coroutineContext)
                }
            }
        }
    }

    @Test
    fun `companion object 테스트`() = runTest {
        val coroutineName = coroutineContext[CoroutineName.Key]
        val key = CoroutineName.Key
    }
}
