package com.example.springstudy.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class MvcWithCoroutineController {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/threadTest")
    suspend fun threadTest(): String {
        val value = runBlocking {
            logger.info("Outer Thread Test")
            val job = scope.launch {
                logger.info("Inner Thread Test")
            }

            logger.info("finished Thread Test")

            return@runBlocking "abc"
        }

        logger.info("value: $value")

        return value
    }
}
