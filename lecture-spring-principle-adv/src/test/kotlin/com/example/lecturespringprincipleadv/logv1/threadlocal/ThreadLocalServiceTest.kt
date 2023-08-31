package com.example.lecturespringprincipleadv.logv1.threadlocal

import com.example.lecturespringprincipleadv.logv1.threadlocal.code.FieldService
import com.example.lecturespringprincipleadv.logv1.threadlocal.code.ThreadLocalFieldService
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class ThreadLocalServiceTest {

    private val fieldService = ThreadLocalFieldService()
    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun 순차_접근_테스트() {
        log.info("start")

        val f1 = Thread {
            fieldService.logic("u1")
        }

        val f2 = Thread {
            fieldService.logic("u2")
        }

        f1.start()
        Thread.sleep(2000)
        f2.start()

        Thread.sleep(3000)
    }

    @Test
    fun 동시_접근_테스트() {
        log.info("start")

        val f1 = Thread {
            fieldService.logic("u1")
        }

        val f2 = Thread {
            fieldService.logic("u2")
        }

        f1.start()
        f2.start()

        Thread.sleep(3000)
    }
}
