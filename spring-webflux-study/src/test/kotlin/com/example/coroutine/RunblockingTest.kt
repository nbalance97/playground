package com.example.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class RunblockingTest {

    val logger = LoggerFactory.getLogger(javaClass)

    @Test
    fun `runBlocking과 내부 Dispatchers-IO는 parallel call이 가능하다`() = runBlocking {
        val deferred1 = async {
            withContext(Dispatchers.IO) {
                delay(1000)
                logger.info("hello world in IO")
            }
        }

        val deferred2 = async {
            withContext(Dispatchers.IO) {
                delay(500)
                logger.info("hello world in IO")
            }
        }

        deferred1.await()
        deferred2.await()
        logger.info("hello world in IO")
    }
}
