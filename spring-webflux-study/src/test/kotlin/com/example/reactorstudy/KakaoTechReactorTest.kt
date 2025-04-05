package com.example.reactorstudy

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toFlux
import java.lang.Thread.sleep

/**
 * 참고 : 카카오 기술 블로그
 * <a href="https://tech.kakao.com/posts/350">Kotlin Reactor</a>
 */
class KakaoTechReactorTest {

    val logger = LoggerFactory.getLogger(javaClass)

    val basket1 = listOf("kiwi", "orange", "lemon", "kiwi", "orange", "lemon")
    val basket2 = listOf("banana", "lemon", "lemon")
    val basket3 = listOf("strawberry", "orange", "lemon", "grape", "strawberry")

    val baskets = listOf(basket1, basket2, basket3)
    val basketFlux = baskets.toFlux() // == Flux.fromIterable(baskets)

    @Test
    fun case1() {
        /**
         * 요구사항 -> 바구니속 과일 종류 및 각 종류별 개수 나누기
         */

        basketFlux.concatMap {
            val distinctFruitsMono = it.toFlux().distinct().collectList()

            val countFruitsMono = it.toFlux()
                .groupBy { it }
                .concatMap { grouppedFlux ->
                    grouppedFlux.count().map {
                        mapOf(grouppedFlux.key() to it)
                    }
                }
                .reduce { acc, map -> acc + map }

            return@concatMap Mono.empty<String>()
        }
    }

    @Test
    fun case2_with_zip() {
        /**
         * 요구사항 -> 바구니속 과일 종류 및 각 종류별 개수 나누기 + zip 써서 마무리까지 짓기
         */

        data class FruitInfo(val distinctFruits: List<String>, val countFruits: Map<String, Long>)

        basketFlux.concatMap { basket ->
            val distinctFruitsMono = basket.toFlux().distinct().log().collectList()

            val countFruitsMono = basket.toFlux()
                .log()
                .groupBy { fruit -> fruit }
                .concatMap { grouppedFlux ->
                    grouppedFlux.count().map { count ->
                        mapOf(grouppedFlux.key() to count)
                    }
                }
                .reduce { acc, map -> acc + map }

            return@concatMap Flux.zip(distinctFruitsMono, countFruitsMono) { distinctFruit, countFruit ->
                FruitInfo(distinctFruit, countFruit)
            }
        }.subscribe { println(it) }

        sleep(1000)
    }

    @Test
    fun 스케쥴러_적용해보기() {

        data class FruitInfo(val distinctFruits: List<String>, val countFruits: Map<String, Long>)

        basketFlux.concatMap { basket ->
            val distinctFruitsMono = basket.toFlux()
                .distinct()
                .collectList()
                .subscribeOn(Schedulers.parallel())

            val countFruitsMono = basket.toFlux()
                .groupBy { fruit -> fruit }
                .concatMap { grouppedFlux ->
                    grouppedFlux.count().map { count ->
                        mapOf(grouppedFlux.key() to count)
                    }
                }
                .reduce { acc, map -> acc + map }
                .subscribeOn(Schedulers.parallel())

            return@concatMap Flux.zip(distinctFruitsMono, countFruitsMono) { distinctFruit, countFruit ->
                FruitInfo(distinctFruit, countFruit)
            }
        }.subscribe {
            logger.info("${Thread.currentThread().isDaemon} - data : ${it}")
        }

        logger.info("${Thread.currentThread().isDaemon} - hello world!")
        sleep(1000)
    }

    @Test
    fun with_log() {

        data class FruitInfo(val distinctFruits: List<String>, val countFruits: Map<String, Long>)

        basketFlux.concatMap { basket ->
            val distinctFruitsMono = basket.toFlux()
                .log()
                .distinct()
                .collectList()
                .subscribeOn(Schedulers.parallel())

            val countFruitsMono = basket.toFlux()
                .log()
                .groupBy { fruit -> fruit }
                .concatMap { grouppedFlux ->
                    grouppedFlux.count().map { count ->
                        mapOf(grouppedFlux.key() to count)
                    }
                }
                .reduce { acc, map -> acc + map }
                .subscribeOn(Schedulers.parallel())

            return@concatMap Flux.zip(distinctFruitsMono, countFruitsMono) { distinctFruit, countFruit ->
                FruitInfo(distinctFruit, countFruit)
            }
        }.subscribe {
            logger.info("data : ${it}")
        }

        sleep(1000)
    }
}
