package com.example.lecturespringprincipleadv.logv1.hellotracev3

import com.example.lecturespringprincipleadv.logv1.TraceId
import com.example.lecturespringprincipleadv.logv1.TraceStatus
import org.slf4j.LoggerFactory

/**
 * ThreadLocal의 단점
 * - ThreadLocal.remove()를 호출하지 않으면, 이후 해당 스레드를 사용하는 다른 사용자에 의해 이전에 사용한ThreadLocal 내부의 값이 드러날 수 있다.
 * - filter / interceptor에서 threadlocal을 remove() 해주는 작업이 꼭 필요하다
 */
class ThreadLocalLogTrace(
    private var traceIdHolder: ThreadLocal<TraceId?> = ThreadLocal.withInitial { null },
) : LogTrace {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun begin(message: String): TraceStatus {
//        println("traceIdHolder: ${traceIdHolder?.level}")
        traceIdHolder.set(traceIdHolder?.get()?.generateNextId() ?: TraceId())
        return beginSync(traceIdHolder.get()!!, message)
    }

    override fun end(status: TraceStatus) {
        complete(status, null)
    }

    override fun exception(status: TraceStatus, e: Exception) {
        complete(status, e)
    }

    fun beginSync(beforeTraceId: TraceId, message: String): TraceStatus {
        val traceId = beforeTraceId.generateNextId()
        val startTimeMs = System.currentTimeMillis()
        log.info("[{}] {}{}", traceId.traceId, "${"|".repeat(traceId.level)}-->", message)
        return TraceStatus(traceId, startTimeMs, message)
    }

    private fun complete(status: TraceStatus, e: Exception?) {
        val elapsedTime  = System.currentTimeMillis() - status.startTimeMs
        val traceId = status.traceId
        val message = status.message
        e?.let {
            log.info("[{}] {}{}, time: {} ms", traceId.traceId, "${"|".repeat(traceId.level)}<X-", message, elapsedTime)
        } ?: log.info("[{}] {}{}, time: {} ms", traceId.traceId, "${"|".repeat(traceId.level)}<--", message, elapsedTime)

        releaseTraceId()
    }

    private fun releaseTraceId() {
        val isFirstLevel = traceIdHolder.get()?.isFirstLevel() ?: return
        if (isFirstLevel) {
            traceIdHolder.remove()
        } else {
            val traceId = traceIdHolder.get()?.generatePreviousId() ?: return
            traceIdHolder.set(traceId)
        }
    }
}
