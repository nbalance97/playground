package com.example.lecturespringprincipleadv.logv1.hellotracev3

import com.example.lecturespringprincipleadv.logv1.TraceId
import org.springframework.stereotype.Service

@Service
class OrderServiceV3(
    private val orderRepository: OrderRepositoryV3,
    private val trace: LogTrace,
) {
    fun orderItem(itemId: String) {
        val status = trace.begin("OrderServiceV3.request()")
        try {
            orderRepository.save(itemId)
            trace.end(status)
        } catch (e: Exception) {
            trace.exception(status, e)
            throw e // 예외를 rethrow 하여 실행 흐름 바꾸지 않도록
        }
    }
}
