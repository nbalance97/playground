package com.example.lecturespringprincipleadv.logv1.hellotracev2

import com.example.lecturespringprincipleadv.logv1.hellotracev3.FieldLogTrace
import org.junit.jupiter.api.Test

class HelloTraceV3Test {

    @Test
    fun test() {
        val fieldLogTrace = FieldLogTrace(null)

        val status1 = fieldLogTrace.begin("abc")
        val status2 = fieldLogTrace.begin("def")
        fieldLogTrace.exception(status2, IllegalStateException())
        fieldLogTrace.exception(status1, IllegalStateException())
    }
}
