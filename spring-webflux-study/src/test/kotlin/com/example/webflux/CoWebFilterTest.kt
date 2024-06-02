package com.example.webflux

import org.springframework.web.server.CoWebFilter
import org.springframework.web.server.CoWebFilterChain
import org.springframework.web.server.ServerWebExchange

/**
 * CoWebFilter : Kotlin Coroutine을 사용하기 위한 WebFilter
 */
class CoWebFilterTest {

    class TestFilter : CoWebFilter() {
        override suspend fun filter(exchange: ServerWebExchange, chain: CoWebFilterChain) {


            return chain.filter(exchange)
        }
    }
}
