package com.example.reactorstudy

import org.junit.jupiter.api.Test
import reactor.core.publisher.BufferOverflowStrategy
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.time.Duration

class BackPressureTest {

    @Test
    fun BackPressureTest_Error() {
        Flux.interval(Duration.ofMillis(1))
            .onBackpressureError()
            .doOnNext { println("product -> ${it}") }
            .publishOn(Schedulers.parallel())
            .subscribe ({
                Thread.sleep(100)

                println("consume -> ${it}")
            }, { println(it) })


        Thread.sleep(100000)
    }

    @Test
    fun BackPressureTest_Drop() {
        Flux.interval(Duration.ofMillis(1))
            .onBackpressureDrop { droppedElement -> println("drop -> ${droppedElement}") }
            .doOnNext { println("product -> ${it}") }
            .publishOn(Schedulers.parallel())
            .subscribe ({
                Thread.sleep(5)

                println("consume -> ${it}")
            }, { println(it) })


        Thread.sleep(100000)
    }

    @Test
    fun BackPressureTest_Latest() {
        Flux.interval(Duration.ofMillis(1))
            .onBackpressureLatest()
            .doOnNext { println("product -> ${it}") }
            .publishOn(Schedulers.parallel())
            .subscribe ({
                Thread.sleep(5)

                println("consume -> ${it}")
            }, { println(it) })


        Thread.sleep(100000)
    }

    @Test
    fun BackPressureTest_Buffer_DROP_LATEST() {
        Flux.interval(Duration.ofMillis(1))
            .onBackpressureBuffer(2, { println("overflow -> ${it}") }, BufferOverflowStrategy.DROP_LATEST)
            .doOnNext { println("product -> ${it}") }
            .publishOn(Schedulers.parallel(), false, 1)
            .subscribe ({
                Thread.sleep(5)

                println("consume -> ${it}")
            }, { println(it) })


        Thread.sleep(100000)
    }

    @Test
    fun BackPressureTest_Buffer_DROP_OLDEST() {
        Flux.interval(Duration.ofMillis(1))
            .onBackpressureBuffer(2, { println("overflow -> ${it}") }, BufferOverflowStrategy.DROP_OLDEST)
            .doOnNext { println("product -> ${it}") }
            .publishOn(Schedulers.parallel(), false, 1)
            .subscribe ({
                Thread.sleep(5)

                println("consume -> ${it}")
            }, { println(it) })


        Thread.sleep(100000)
    }
}
