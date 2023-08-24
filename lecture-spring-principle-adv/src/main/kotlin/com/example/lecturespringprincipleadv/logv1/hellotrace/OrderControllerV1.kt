package com.example.lecturespringprincipleadv.logv1.hellotrace

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderControllerV1(
    private val orderService: OrderServiceV1,
    private val trace: HelloTraceV1
) {

    @GetMapping("/v1/request")
    fun request(itemId: String): String {
        val status = trace.begin("OrderControllerV1.request()")
        try {
            orderService.orderItem(itemId)
            trace.end(status)
            return "ok"
        } catch (e: Exception) {
            trace.exception(status, e)
            throw e // 예외를 rethrow 하여 실행 흐름 바꾸지 않도록
        }
    }
}
