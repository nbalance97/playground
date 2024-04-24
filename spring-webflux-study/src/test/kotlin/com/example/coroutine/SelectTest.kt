package com.example.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SelectTest {

    @Test
    fun `가장 빨리 종료되는 작업 대기 테스트`(): Unit = runBlocking(Dispatchers.Default) {
        val a = async {
            delay(1000)
            1
        }

        val b = async {
            delay(2000)
            2
        }

        val result = select {
            a.onAwait { it }
            b.onAwait { it }
        }

        assertThat(result).isEqualTo(1)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `채널에서 값을 선택하는 케이스`() = runTest {
        fun CoroutineScope.produceNumbers(i: Int, time: Long): ReceiveChannel<Any> {
            return produce {
                while (true) {
                    delay(time)
                    send(i)
                }
            }
        }

        val aChannel = produceNumbers(1, 100L)
        val bChannel = produceNumbers(2, 200L)

        repeat(7) {
            val result = select {
                aChannel.onReceive { it }
                bChannel.onReceive { it }
            }

            println(result)
        }

        coroutineContext.cancelChildren()
    }

    @Test
    fun `채널에 값을 전송 및 수신하는 케이스`() = runTest {
        val channelA = Channel<Int>(capacity = 2)
        val channelB = Channel<Int>(capacity = 2)

        launch {
            repeat(5) {
                /**
                 * expected : A -> A -> B -> B -> A
                 */
                select {
                    channelA.onSend(it) { println("Sent to channel A") }
                    channelB.onSend(it) { println("Sent to channel B") }
                }

                println("send $it")
            }
        }

        delay(100) // 데이터 전달 잠시 대기

        launch {
            repeat(5) {
                select {
                    channelA.onReceive { println("Received from channel A - $it") }
                    channelB.onReceive { println("Received from channel B - $it") }
                }
            }
        }

        delay(100) // 모든 데이터 컨슘 대기

        coroutineContext.cancelChildren()
    }
}
