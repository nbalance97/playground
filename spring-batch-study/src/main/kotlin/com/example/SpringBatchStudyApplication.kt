package com.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

// @EnableBatchProcessing (springboot 3.0+에서는 필요 없음, 오히려 EnableBatchProcessing을 넣었을때 에러가 발생하는 것으로 보임.)
@SpringBootApplication
class SpringBatchStudyApplication

fun main(args: Array<String>) {
    runApplication<SpringBatchStudyApplication>(*args)
}

