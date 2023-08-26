package com.example.lecturespringprincipleadv.logv1.hellotracev3

import com.example.lecturespringprincipleadv.logv1.TraceId
import com.example.lecturespringprincipleadv.logv1.TraceStatus
import org.slf4j.LoggerFactory

class FieldLogTrace(
    private var traceIdHolder: TraceId?
) : LogTrace {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun begin(message: String): TraceStatus {
        traceIdHolder = traceIdHolder?.generateNextId() ?: TraceId()
        return beginSync(traceIdHolder!!, message)
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
        log.info("[{}] {}{}", traceId.traceId, "${"| ".repeat(traceId.level)}-->", message)
        return TraceStatus(traceId, startTimeMs, message)
    }

    private fun complete(status: TraceStatus, e: Exception?) {
        val elapsedTime  = System.currentTimeMillis() - status.startTimeMs
        val traceId = status.traceId
        val message = status.message
        e?.let {
            log.info("[{}] {}{}, time: {} ms", traceId.traceId, "${"| ".repeat(traceId.level)}<X-", message, elapsedTime)
        } ?: log.info("[{}] {}{}, time: {} ms", traceId.traceId, "${"| ".repeat(traceId.level)}<--", message, elapsedTime)

        releaseTraceId()
    }

    private fun releaseTraceId() {
        val isFirstLevel = traceIdHolder?.isFirstLevel() ?: return
        if (isFirstLevel) {
            traceIdHolder = null
        } else {
            traceIdHolder = traceIdHolder?.generatePreviousId() ?: return
        }
    }
}
