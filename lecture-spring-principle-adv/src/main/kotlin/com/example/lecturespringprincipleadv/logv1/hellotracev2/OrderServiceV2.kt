package com.example.lecturespringprincipleadv.logv1.hellotracev2

import com.example.lecturespringprincipleadv.logv1.TraceId
import org.springframework.stereotype.Service

@Service
class OrderServiceV2(
    private val orderRepository: OrderRepositoryV2,
    private val trace: HelloTraceV2,
) {
    fun orderItem(traceId: TraceId, itemId: String) {
        val status = trace.beginSync(traceId, "OrderServiceV1.request()")
        try {
            trace.end(status)
            orderRepository.save(status.traceId, itemId)
        } catch (e: Exception) {
            trace.exception(status, e)
            throw e // 예외를 rethrow 하여 실행 흐름 바꾸지 않도록
        }
    }
}
