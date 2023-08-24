package com.example.lecturespringprincipleadv.logv1.hellotrace

import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryV1(
    private val trace: HelloTraceV1
) {
    fun save(itemId: String) {
        val status = trace.begin("OrderRepositoryV1.request()")
        try {
            throw IllegalStateException()
            trace.end(status)
        } catch (e: Exception) {
            trace.exception(status, e)
            throw e // 예외를 rethrow 하여 실행 흐름 바꾸지 않도록
        }
    }
}
