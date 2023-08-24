package com.example.lecturespringprincipleadv.logv1.hellotracev2

import com.example.lecturespringprincipleadv.logv1.TraceId
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryV2(
    private val trace: HelloTraceV2,
) {
    fun save(traceId: TraceId, itemId: String) {
        val status = trace.beginSync(traceId, "OrderRepositoryV1.request()")
        try {
            throw IllegalStateException()
            trace.end(status)
        } catch (e: Exception) {
            trace.exception(status, e)
            throw e // 예외를 rethrow 하여 실행 흐름 바꾸지 않도록
        }
    }
}
