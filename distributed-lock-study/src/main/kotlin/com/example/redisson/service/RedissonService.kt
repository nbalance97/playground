package com.example.redisson.service

import org.redisson.api.RedissonClient
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit


@Service
class RedissonService(
    private val client: RedissonClient
) {

    private val waitTime = 10L
    private val leaseTime = 2L

    var sharedVariable = 10

    fun incrementWithoutLock() {
        println("increment 호출")

        getAndIncrement()
    }

    fun incrementWithLock() {
        val lock = client.getLock("lock")
        println("increment 호출")

        // if waitTime만큼 대기, 10초 동안 잠금 사용
        if (lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
            println("Lock 획득")

            try {
                getAndIncrement()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (lock.isHeldByCurrentThread)
                    lock.unlock()
                println("Lock 반환")
            }
        }

        println("increment 종료")
    }

    /**
     * Proxy [ ... aop ]
     * f()
     *
     * aop Transactional ??
     * [ Transactional(r ..., Transactional, ]
     * begin
     * commit
     *
     * 실행 결과가 같음 (범위 동일)
     *
     * begin
     * begin
     * commit
     * commit
     *
     */

    @Transactional
    fun f() {
        aop_f()
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun aop_f() {
        f()
    }

    private fun getAndIncrement() {
        val before = sharedVariable
        sleep(1000)
        sharedVariable = before + 1
    }
}
