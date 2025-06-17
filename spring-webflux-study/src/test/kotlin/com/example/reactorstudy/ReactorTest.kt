package com.example.reactorstudy

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.test.StepVerifier
import java.time.Duration

class ReactorTest {

    val logger = LoggerFactory.getLogger(javaClass)

    @Test
    fun 모든_데이터를_방출할때까지_기다리는_테스트() {
        val flux = Flux.just(1, 2, 3)
            .flatMap {
                Mono.just(it)
                    .delayElement(Duration.ofSeconds(1))
                    .map { it * 100 }
            }
            .doOnNext { println(it) }

        StepVerifier.create(flux)
            .thenConsumeWhile { true }
            .verifyComplete()
    }

    @Test
    fun Reactor() {
        Flux.just(1, 2, 3)
            //.publishOn(Schedulers.single())
            .doOnNext { logger.info("#doOnNext: $it")}
            .flatMap {
                Mono.just(it)
                    .delayElement(Duration.ofSeconds(1))
                    .doOnNext { logger.info("#Mono.doOnNext: $it") }
                    .map { it * 100 }
            }
            //.publishOn(Schedulers.boundedElastic())
            .doOnNext { logger.info("준최종 : $it") }
            .map { it -> it }
            .doOnNext { logger.info("최종 : $it") }
            .blockLast()
    }

    @Test
    fun flatMap() {
        val testPipeline = Flux.just(1, 2, 3, 4, 5)
            .doOnNext { logger.info("#doOnNext: $it")}
            .flatMap {
                Mono.just(it)
                    .publishOn(Schedulers.boundedElastic()) // boundedElastic이 없으면 flatMap 내부 작업이 Parallel하게 실행되지 않는다.
                    .map {
                        Thread.sleep(1000)
                        logger.info("#flatMap data -> ${it}")
                        it
                    }
            }

        StepVerifier.create(testPipeline)
            .thenConsumeWhile { true }
            .verifyComplete()
    }
}
