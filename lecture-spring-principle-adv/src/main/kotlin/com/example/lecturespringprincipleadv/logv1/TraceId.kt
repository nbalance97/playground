package com.example.lecturespringprincipleadv.logv1

import java.util.*

class TraceId(
    val traceId: String,
    val level: Int = 0,
) {
    constructor() : this(
        traceId = UUID.randomUUID().toString().substring(0 .. 8),
        level = 0
    )

    // 다음 레벨의 ID 생성
    fun generateNextId(): TraceId = TraceId(traceId, level + 1)

    fun generatePreviousId(): TraceId = TraceId(traceId, level - 1)

    fun isFirstLevel(): Boolean = level == 0
}
