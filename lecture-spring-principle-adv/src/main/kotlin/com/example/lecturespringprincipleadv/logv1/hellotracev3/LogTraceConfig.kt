package com.example.lecturespringprincipleadv.logv1.hellotracev3

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LogTraceConfig {

    @Bean
    fun logTrace(): FieldLogTrace = FieldLogTrace(null)
}
