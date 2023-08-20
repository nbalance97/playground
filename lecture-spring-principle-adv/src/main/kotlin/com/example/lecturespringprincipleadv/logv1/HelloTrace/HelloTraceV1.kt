package com.example.lecturespringprincipleadv.logv1.HelloTrace

import com.example.lecturespringprincipleadv.logv1.TraceId
import com.example.lecturespringprincipleadv.logv1.TraceStatus
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component


@Component
class HelloTraceV1 {

    /**
     * Kotlin에서는 Slf4j로 로그가 찍히지 않는다
     * LoggerFactory.getLogger로 따로 로거를 만들어 주는게 일반적인 컨벤션인듯? - 다른 편의성을 고려한 개선을 하려곤 하는데.. 굳이?
     */
    private val log = LoggerFactory.getLogger(javaClass)

    fun begin(message: String): TraceStatus {
        val traceId = TraceId()
        val startTimeMs = System.currentTimeMillis()
        log.info("[{}] {}{}", traceId.traceId, "${"| ".repeat(traceId.level)}-->", message)
        return TraceStatus(traceId, startTimeMs, message)
    }

    fun end(traceStatus: TraceStatus) = complete(traceStatus, null)

    fun exception(status: TraceStatus, e: Exception) = complete(status, e)

    private fun complete(status: TraceStatus, e: Exception?) {
        val elapsedTime  = System.currentTimeMillis() - status.startTimeMs
        val traceId = status.traceId
        val message = status.message
        e?.let {
            log.info("[{}] {}{}, time: {} ms", traceId.traceId, "${"| ".repeat(traceId.level)}<X-", message, elapsedTime)
        } ?: log.info("[{}] {}{}, time: {} ms", traceId.traceId, "${"| ".repeat(traceId.level)}<--", message, elapsedTime)
    }
}
