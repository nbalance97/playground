package com.example.reactorstudy.basicTest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 언제 coroutineScope를 사용할까?
 * - parallel call을 할때 사용하는 것으로 보임
 *
 * webflux에서 controller에 suspend를 붙이니까.. 전체적으로 non-blocking이지 않을까 싶음
 */
@RestController
class CoroutineController(
    private val coroutineService: CoroutineService
) {

    @GetMapping("/test")
    suspend fun asyncTest() {
        coroutineService.longTermFun()
    }
}
