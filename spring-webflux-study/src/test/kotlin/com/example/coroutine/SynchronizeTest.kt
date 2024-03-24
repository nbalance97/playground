package com.example.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class SynchronizeTest {

    @Test
    fun `Race_condition_테스트`() = runTest {
        val innerTestClass = InnerTestClass()

        coroutineScope {
            for (i in 0..10) {
                launch(Dispatchers.Default) {
                    println("action -> $i - thread : ${Thread.currentThread().name}")
                    val data = innerTestClass.count
                    Thread.sleep(100)
                    innerTestClass.count = data + 1
                }
            }
        }

        println(innerTestClass.count)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Dispatcher의 동시성을 제한하여 동시성 문제 해소`() = runTest {
        val innerTestClass = InnerTestClass()
        val singleThreadDispatcher = Dispatchers.Default.limitedParallelism(1)

        coroutineScope {
            for (i in 0..10) {
                launch(singleThreadDispatcher) {
                    println("action -> $i - thread : ${Thread.currentThread().name}")
                    val data = innerTestClass.count
                    Thread.sleep(100)
                    innerTestClass.count = data + 1
                }
            }
        }

        println(innerTestClass.count)
    }

    @Test
    fun `Mutex를 이용하여 동시성 해소`() = runTest {
        val innerTestClass = InnerTestClass()
        val mutex = Mutex()

        /**
         * lock()과 unlock()으로 잠금을 얻거나 반환할수 있음.
         * mutex에서의 lock(), unlock()을 직접 사용하는건 비권장 (예외 발생시 어떻게 할지..?)
         */
//        coroutineScope {
//            for (i in 0..10) {
//                launch(Dispatchers.Default) {
//                    mutex.lock()
//                    println("action -> $i - thread : ${Thread.currentThread().name}")
//                    val data = innerTestClass.count
//                    Thread.sleep(100)
//                    innerTestClass.count = data + 1
//                    mutex.unlock()
//                }
//            }
//        }

        /**
         * withLock을 사용하면 lock()과 unlock()을 자동으로 처리해줌
         * 예외 발생시 알아서 unlock() 처리 해준다.
         */
        coroutineScope {
            for (i in 0..10) {
                launch(Dispatchers.Default) {
                    mutex.withLock {
                        println("action -> $i - thread : ${Thread.currentThread().name}")
                        val data = innerTestClass.count
                        Thread.sleep(100)
                        innerTestClass.count = data + 1
                    }
                }
            }
        }

        println(innerTestClass.count)
    }

    @Test
    fun `으하하 교착상태가 일어날까?`() = runTest {
        val mutex = Mutex()

        mutex.withLock {
            mutex.withLock {
                println("교착상태 발생")
            }
        }
    }

    class InnerTestClass() {
        var count = 0
    }
}
