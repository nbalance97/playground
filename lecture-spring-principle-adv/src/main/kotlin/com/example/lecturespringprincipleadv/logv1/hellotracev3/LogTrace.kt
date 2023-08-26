package com.example.lecturespringprincipleadv.logv1.hellotracev3

import com.example.lecturespringprincipleadv.logv1.TraceStatus

interface LogTrace {
    fun begin(message: String): TraceStatus
    fun end(status: TraceStatus)
    fun exception(status: TraceStatus, e: Exception)
}
