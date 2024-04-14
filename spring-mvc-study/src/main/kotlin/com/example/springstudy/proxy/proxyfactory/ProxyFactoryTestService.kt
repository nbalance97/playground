package com.example.springstudy.proxy.proxyfactory

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.aop.framework.ProxyFactory


/**
 * ProxyFactory를 사용한 AOP
 * ProxyFactory는 맞추어서 Jdk Dynamic Proxy나 Cglib을 사용한다.
 *
 * 인터페이스가 있다면 -> Jdk Dynamic Proxy
 * 구체 클래스라면 -> Cglib Library
 */
class ProxyAdvice : MethodInterceptor {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun invoke(invocation: MethodInvocation): Any? {
        logger.info("Advice 실행")
        val result = invocation.proceed() // invocation.proceed() 메소드로 MethodInvocation을 실행한다
        logger.info("Advice 종료")

        return result
    }

}

// cglib용 클래스
open class ProxyAdviceTestClass {
    open fun test() {
        println("test")
    }
}

// Jdk Dynamic Proxy용 클래스
interface ProxyAdviceJdkDynamicProxyTestClass {
    fun test()
}

open class ProxyAdviceJdkDynamicProxyTestClassImpl : ProxyAdviceJdkDynamicProxyTestClass {
    override fun test() {
        println("test")
    }
}


class ProxyFactoryTestService {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun test() {

//         1. cglib
//        val target = ProxyAdviceTestClass()
//        val proxyFactory = ProxyFactory(target)
//        proxyFactory.addAdvice(ProxyAdvice())
//        val proxy = proxyFactory.proxy as ProxyAdviceTestClass

        // 2. Jdk Dynamic Proxy
//        val target: ProxyAdviceJdkDynamicProxyTestClass = ProxyAdviceJdkDynamicProxyTestClassImpl()
//        val proxyFactory = ProxyFactory(target)
//        proxyFactory.addAdvice(ProxyAdvice())
//        val proxy = proxyFactory.proxy as ProxyAdviceJdkDynamicProxyTestClass

        // 3. 인터페이스의 구현체임에도 Cglib을 쓰고싶을때 isProxyTargetClass 속성 추가
        val target: ProxyAdviceJdkDynamicProxyTestClass = ProxyAdviceJdkDynamicProxyTestClassImpl()
        val proxyFactory = ProxyFactory(target)
        proxyFactory.addAdvice(ProxyAdvice())
        proxyFactory.isProxyTargetClass = true // cglib을 사용하기 위해 "구체 클래스" 를 대상으로 프록시 생성
        // Spring AOP는 기본적으로 CGLIB으로 프록시를 생성한다 (항상 isProxyTargetClass = true)
        val proxy = proxyFactory.proxy as ProxyAdviceJdkDynamicProxyTestClass

        logger.info("target : ${target.javaClass}")
        logger.info("proxy : ${proxy.javaClass}")

        proxy.test()
    }
}
