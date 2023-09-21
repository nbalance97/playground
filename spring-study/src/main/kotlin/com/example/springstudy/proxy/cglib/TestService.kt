package com.example.springstudy.proxy.cglib


import org.slf4j.LoggerFactory
import org.springframework.cglib.proxy.Enhancer
import org.springframework.cglib.proxy.MethodInterceptor
import org.springframework.cglib.proxy.MethodProxy
import java.lang.reflect.Method

/**
 * final class -> CGlib 설정이 불가능하다
 * 코틀린에서는 기본적으로 final class (plugin의 도움으로 자동으로 @Service같은 어노테이션이 붙으면 Open으로 간주됨)
 *
 * + 코틀린은 class만 open으로 한다고 다가 아니다. cglib에서 method도 open 붙여주자
 */


open class TestService {
    val logger = LoggerFactory.getLogger(javaClass)

    open fun test(): String {
        // 코틀린은 class만 open으로 한다고 다가 아니다. cglib에서 method도 open 붙여주자

        logger.info("test 실행")
        return "a"
    }
}

open class TestServiceUsage {

    fun test() {
        val enhancer = Enhancer()
        enhancer.setSuperclass(TestService::class.java)
        enhancer.setCallback(CglibInterceptor())
        val proxy = enhancer.create() as TestService

        println(proxy.test())
    }
}

open class CglibInterceptor : MethodInterceptor {

    val logger = LoggerFactory.getLogger(javaClass)

    override fun intercept(obj: Any?, method: Method?, args: Array<out Any>?, proxy: MethodProxy?): Any {
        logger.info("CglibInterceptor 실행")
        val result = proxy?.invokeSuper(obj, args) ?: "xxxx"
        logger.info("CglibInterceptor 종료")

        return "b"
    }
}

