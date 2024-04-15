package com.example.redisson.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.lang.Thread.sleep


@SpringBootTest
class RedissonServiceTest {

    @Autowired
    lateinit var redissonService: RedissonService

    @BeforeEach
    fun init() {
        redissonService.sharedVariable = 0
    }

    @Test
    fun incrementWithoutLock() {
        for (i in 1..5) {
            Thread {
                redissonService.incrementWithoutLock()
            }.start()
        }

        sleep(6000)
        println(redissonService.sharedVariable)
    }

    @Test
    fun incrementWithLock() {
        for (i in 1..5) {
            Thread {
                redissonService.incrementWithLock()
            }.start()
        }

        sleep(6000)

        println(redissonService.sharedVariable)
    }
}
