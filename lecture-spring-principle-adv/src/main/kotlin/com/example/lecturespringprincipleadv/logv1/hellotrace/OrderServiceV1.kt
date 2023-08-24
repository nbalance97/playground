package com.example.lecturespringprincipleadv.logv1.hellotrace

import org.springframework.stereotype.Service

@Service
class OrderServiceV1(
    private val orderRepository: OrderRepositoryV1,
    private val trace: HelloTraceV1
) {
    fun orderItem(itemId: String) {
        val status = trace.begin("OrderServiceV1.request()")
        try {
            trace.end(status)
            orderRepository.save(itemId)
        } catch (e: Exception) {
            trace.exception(status, e)
            throw e // 예외를 rethrow 하여 실행 흐름 바꾸지 않도록
        }
    }
}
