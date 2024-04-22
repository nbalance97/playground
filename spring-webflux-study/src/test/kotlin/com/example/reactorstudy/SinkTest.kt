package com.example.reactorstudy

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.core.scheduler.Schedulers
import java.lang.Thread.sleep

class SinkTest {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Test
    fun `flux create with fluxsink 테스트`() {
        Flux.create { sink -> (0..10).forEach { sink.next(it) } }
            .subscribeOn(Schedulers.boundedElastic())
            .doOnNext { logger.info("# onNext: $it") }
            .publishOn(Schedulers.parallel())
            .map { "$it - success" }
            .doOnNext { logger.info("# onNext2: $it") }
            .publishOn(Schedulers.parallel())
            .subscribe { logger.info("# subscribe: $it") }

        Thread.sleep(5000)
    }

    @Test
    fun `sink one 테스트`() {
        val sink = Sinks.one<Int>()
        val mono = sink.asMono()

        sink.emitValue(1, Sinks.EmitFailureHandler.FAIL_FAST)

        mono.subscribe { logger.info("# subscribe: $it") }
        mono.subscribe { logger.info("# subscribe2: $it") }

        Thread.sleep(1000)
    }

    @Test
    fun `unicast sink 테스트`() {
        val sink = Sinks.many().unicast().onBackpressureBuffer<Int>()

        // FAIL_FAST : Retry등 작업을 하지 않고 바로 Fail 처리(그래서 FAIL_FAST..?)
        sink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST)

        sink.asFlux()
            .subscribe { logger.info("# subscribe: $it") }
    }

    @Test
    fun `unicast sink에서 두번 subscribe 하면 예외가 발생한다`() {
        val sink = Sinks.many().unicast().onBackpressureBuffer<Int>()

        // FAIL_FAST : Retry등 작업을 하지 않고 바로 Fail 처리(그래서 FAIL_FAST..?)
        sink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST)

        sink.asFlux()
            .subscribe { logger.info("# subscribe: $it") }

        assertThrows<IllegalStateException> {
            sink.asFlux()
                .blockLast()
        }
    }

    @Test
    fun `multicast sink에서의 subscribe 테스트`() {
        val sink = Sinks.many().multicast().onBackpressureBuffer<Int>()

        sink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST) // -- subscribe 1에만 전달

        sink.asFlux()
            .subscribe { logger.info("# subscribe1: $it") }

        sink.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST) // -- subscribe 1에만 전달

        sink.asFlux()
            .subscribe { logger.info("# subscribe2: $it") }

        sink.emitNext(3, Sinks.EmitFailureHandler.FAIL_FAST) // -- subscribe 1, 2에 전달

        sleep(1000)
    }

    @Test
    fun `retryable sink에서의 subscribe 테스트`() {
        // 최대 2개까지 replay해주는 sink
        val sink = Sinks.many().replay().limit<Int>(2)

        sink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST)

        sink.asFlux()
            .subscribe { logger.info("# subscribe1: $it") }

        sink.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST)

        sink.asFlux()
            .subscribe { logger.info("# subscribe2: $it") }
        // multicast, unicast와는 다르게 1번까지 모두 가져오는 것을 볼 수 있음

        sleep(1000)
    }
}
