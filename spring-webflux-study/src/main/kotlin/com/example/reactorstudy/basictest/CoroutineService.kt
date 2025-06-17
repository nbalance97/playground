package com.example.reactorstudy.basictest

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import kotlin.coroutines.coroutineContext

@Service
class CoroutineService {

    suspend fun longTermFun() = coroutineScope {
        println("longTermFun start")
        Thread.sleep(1000)
        println("longTermFun end")

        anotherFunc()
    }

    suspend fun anotherFunc() {
        withContext(coroutineContext) {
            print(coroutineContext)
        }
    }
}
