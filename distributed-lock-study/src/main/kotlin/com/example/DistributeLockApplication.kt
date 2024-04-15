package com.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DistributeLockApplication

fun main(args: Array<String>) {
    runApplication<DistributeLockApplication>(*args)
}
