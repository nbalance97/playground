package com.example.lecturespringprincipleadv.logv1.hellotracev2

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderControllerV2(
    private val orderService: OrderServiceV2,
    private val trace: HelloTraceV2,
) {

    @GetMapping("/v2/request")
    fun request(itemId: String): String {
        val status = trace.begin("OrderControllerV2.request()")
        try {
            orderService.orderItem(status.traceId, itemId)
            trace.end(status)
            return "ok"
        } catch (e: Exception) {
            trace.exception(status, e)
            throw e // 예외를 rethrow 하여 실행 흐름 바꾸지 않도록
        }
    }
}
