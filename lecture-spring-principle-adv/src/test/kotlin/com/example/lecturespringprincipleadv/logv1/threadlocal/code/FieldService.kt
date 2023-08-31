package com.example.lecturespringprincipleadv.logv1.threadlocal.code

import org.slf4j.LoggerFactory

class FieldService(
    private var nameStore: String = "none",
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun logic(name: String): String {
        log.info("저장 name={} -> nameStore={}", name, nameStore)
        nameStore = name

        Thread.sleep(1000)

        log.info("조회 nameStore={}", nameStore)
        return nameStore
    }
}
