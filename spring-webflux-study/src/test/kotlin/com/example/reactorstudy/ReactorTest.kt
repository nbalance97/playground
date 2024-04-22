package com.example.reactorstudy

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Duration

class ReactorTest {

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
}
