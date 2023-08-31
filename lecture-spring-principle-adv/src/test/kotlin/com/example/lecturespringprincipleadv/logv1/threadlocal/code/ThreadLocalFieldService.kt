package com.example.lecturespringprincipleadv.logv1.threadlocal.code

import org.slf4j.LoggerFactory
import java.lang.ThreadLocal

class ThreadLocalFieldService(
    private var nameStore: ThreadLocal<String> = ThreadLocal.withInitial { "none" },
) {

    private val threadLocal = ThreadLocal.withInitial { "none" }
    private val log = LoggerFactory.getLogger(javaClass)

    fun logic(name: String): String {
        log.info("저장 name={} -> nameStore={}", name, nameStore.get())
        nameStore.set(name)

        Thread.sleep(1000)

        log.info("조회 nameStore={}", nameStore.get())
        return nameStore.get()
    }
}
