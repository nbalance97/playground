package com.example.lecturespringprincipleadv.logv1.hellotracev3

import com.example.lecturespringprincipleadv.logv1.TraceId
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryV3(
    private val trace: LogTrace,
) {
    fun save(itemId: String) {
        val status = trace.begin("OrderRepositoryV3.request()")
        try {
            throw IllegalStateException()
            trace.end(status)
        } catch (e: Exception) {
            trace.exception(status, e)
            throw e // 예외를 rethrow 하여 실행 흐름 바꾸지 않도록
        }
    }
}
