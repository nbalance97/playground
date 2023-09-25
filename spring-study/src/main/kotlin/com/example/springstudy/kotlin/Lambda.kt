package com.example.springstudy.kotlin

class Lambda {

    fun lambdaTest() {
        listOf(1).forEach {
            println(it)
        }

        listOf(1).forEach(fun (it: Int) {
            println(it)
        })
    }

}
