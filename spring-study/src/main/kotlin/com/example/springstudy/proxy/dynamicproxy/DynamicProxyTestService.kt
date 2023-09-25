package com.example.springstudy.proxy.dynamicproxy

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class DynamicProxyTestService {

    fun test() {
        val proxy = Proxy.newProxyInstance(
            javaClass.classLoader,
            arrayOf(JdkTestServiceIf::class.java),
            JdkServiceInvocationHandler(JdkTestService())
        ) as JdkTestServiceIf

        println(proxy.test())
    }
}

interface JdkTestServiceIf {
    fun test(): String
}

class JdkTestService : JdkTestServiceIf {
    override fun test(): String {
        println("test 실행")
        return "a"
    }
}

class JdkServiceInvocationHandler(private val target: Any?) : InvocationHandler {
    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {
        println("proxy 실행, method : ${method?.name}")
        val tunedArgs = args ?: arrayOf()
        // 그냥 Args를 넣어주면 안되고 Spread 하여 넣어주어야 함에 유의
        val result = method?.invoke(target, *tunedArgs) ?: throw RuntimeException("method is null")
        println("proxy 종료")

        return result
    }
}
