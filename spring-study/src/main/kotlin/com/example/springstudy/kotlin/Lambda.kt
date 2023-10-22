package com.example.springstudy.kotlin

class Lambda {

    fun lambdaTest() {
        // 일반적인 람다의 형태
        listOf(1).forEach {
            println(it)
        }

        // 람다 대신 무명함수로 써줄 수도 있다
        listOf(1).forEach(fun (it: Int) {
            println(it)
        })
    }
}
