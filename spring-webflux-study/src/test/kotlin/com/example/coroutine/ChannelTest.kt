package com.example.coroutine

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class ChannelTest {

    @Test
    fun `Channel 테스트`() = runTest {

        val channel = Channel<Int>()

        // 채널에 무언가 데이터를 넣는 Producer
        launch {
            repeat(5) {
                Thread.sleep(1000)
                channel.send(it)
            }
        }

        // receive는 channel에 무언가가 들어올때까지 중단된다
        launch {
            repeat(5) {
                val data = channel.receive()
                println("data: $data")
            }
        }
    }

    @Test
    fun `Channel 테스트 2`() = runTest {

        val channel = Channel<Int>()

        // producer
        launch {
            repeat(5) {
                Thread.sleep(1000)
                channel.send(it)
            }

            // 채널을 닫아주어야 소비처에서 consumeEach가 종료된다. (중요)
            channel.close()
        }

        launch {

            // channel에 무언가가 들어오면 바로 출력
            // 단, 유의할 점은 consumeEach는 채널이 닫힐때까지 계속 실행된다
            channel.consumeEach {
                println("data: $it")
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Channel 테스트 3`() = runTest {

        // 위에서는 Channel을 직접 생성했지만, produce 빌더로 더 쉽게 채널을 생성할수 있음
        // Channel의 Close()까지 같이 해준다.
        val channel = produce{
            repeat(5) {
                Thread.sleep(1000)
                println("sent")
                send(it)
            }
        }

        launch {
            channel.consumeEach {
                println("data: $it")
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Channel 테스트(Capacity 설정)`() = runTest {

        // 위에서는 Channel을 직접 생성했지만, produce 빌더로 더 쉽게 채널을 생성할수 있음
        // Channel의 Close()까지 같이 해준다.
        /**
         * Channel.BUFFERED : 버퍼가 가득 찰때까지 원소가 생성되고, 이후 생성자는 소비자가 원소를 소비하길 기다림, 버퍼 기본값은 64 (kotlinx.coroutines.channels.defaultBuffer로 조절 가능)
         * Channel.RENDEZVOUS : 버퍼 없음, 송신자와 수신자가 만날때만 원소 교환
         * - 수신자가 없다면 송신자가 대기하는 것으로 보임 (suspend)
         * Channel.Conflated : 버퍼 크기가 1, 새 원소가 기존 원소를 대체함
         */
        val channel = produce(capacity = Channel.CONFLATED) {
            repeat(5) {
                println("sent")
                delay(200)
                send(it)
            }
        }

        delay(1000)

        launch {
            println("scope2 실행")
            channel.consumeEach {
                println("data: $it")
            }
        }
    }

    @Test
    fun `Channel 테스트(Customize buffer handler)`() = runTest {
        val customizedChannel = Channel<Int>(
            capacity = 2,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

        launch {
            repeat(5) {
                // 0, 1, 2, 3, 4
                customizedChannel.send(it)
                delay(100)
            }

            customizedChannel.close()
        }

        delay(1000)

        launch {
            // onBufferOverflow, capacity 옵션에 따라 출력이 달라짐
            // capacity = 2, onBufferOverflow = BufferOverflow.DROP_OLDEST 상황에서는 3, 4가 나옴
            customizedChannel.consumeEach {
                println("data: $it")
            }
        }
    }

    @Test
    fun `Channel 테스트(resource)`() = runTest {

        class TestObject {

            fun close() {
                println("close")
            }
        }

        val customizedChannel = Channel<TestObject>() { resource -> resource.close() }
        customizedChannel.send(TestObject())
    }

    @Test
    fun `fan Test`() = runTest {

        val sharedChannel = Channel<String>()

        val producer1 = launch {
            repeat(5) {
                sharedChannel.send(it.toString())
                delay(100)
            }
        }

        val producer2 = launch {
            repeat(5) {
                sharedChannel.send((it * 5).toString())
                delay(100)
            }
        }

        val consumer1 = launch {
            sharedChannel.consumeEach {
                println("consumer1: $it")
            }
        }

        val consumer2 = launch {
            sharedChannel.consumeEach {
                println("consumer2: $it")
            }
        }

        delay(500)
        sharedChannel.close()
    }
}
