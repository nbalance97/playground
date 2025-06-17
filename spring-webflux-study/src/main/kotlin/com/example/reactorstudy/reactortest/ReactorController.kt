package com.example.reactorstudy.reactortest

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class ReactorController {

    private val logger = LoggerFactory.getLogger(ReactorController::class.java)

    @GetMapping("/reactor/test")
    fun reactorTest(): Flux<String> {
        return Flux.just("1", "2", "3", "4", "5")
            .doOnNext { logger.info("#doOnNext: $it") }
            .flatMap {
                Mono.just(it)
                    .map {
                        Thread.sleep(1000)
                        logger.info("#flatMap data -> $it")
                        it
                    }
            }
    }
}
