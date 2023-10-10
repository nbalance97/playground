package com.example.springstudy.proxy.proxyfactory

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.springframework.aop.ClassFilter
import org.springframework.aop.MethodMatcher
import org.springframework.aop.Pointcut
import org.springframework.aop.framework.ProxyFactory
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.stereotype.Service
import java.lang.reflect.Method

/**
 * 스프링에서 제공하는 포인트컷
 *
 * 1. NameMatchMethodPointcut : 메소드 이름을 기반으로 매칭
 * 2. JdkRegexpMethodPointcut : Jdk 정규 표현식
 * 3. TruePointcut : 항상 참
 * 4. AnnotationMatchingPointcut : Annotation으로 매칭
 * 5. AspectJExpressionPointcut : aspectJ
 *
 * 가장 중요한 것은 AspectJExpressionPointcut이다
 */

open class AdvisorTargetObj {

    open fun test() {
        println("test")
    }
}

class AdviceTestObj : MethodInterceptor {
    override fun invoke(invocation: MethodInvocation): Any? {
        println("Advice 실행")
        val result = invocation.proceed()
        println("Advice 종료")

        return result
    }
}

class PointcutTestObj : Pointcut {
    override fun getClassFilter(): ClassFilter = ClassFilter.TRUE // 항상 TRUE로 나간다

    override fun getMethodMatcher(): MethodMatcher = MethodMatcherExample("test")


    class MethodMatcherExample(val methodName: String) : MethodMatcher {

        // 입력받은 메소드명과 같아야만 포인트컷에 걸린다
        override fun matches(method: Method, targetClass: Class<*>): Boolean = method.name == methodName

        override fun matches(method: Method, targetClass: Class<*>, vararg args: Any?): Boolean {
            return false
        }

        override fun isRuntime(): Boolean {
            // true이면 matches 메소드의 3번째 파라미터인 args를 사용할 수 있다 (args 체크가 가능, 스프링 내부에서 캐싱이 따로 되진 않는다)
            // false이면 matches 메소드의 3번째 파라미터인 args를 사용할 수 없다 (스프링 내부에서 캐싱이 가능하다. 크게 중요한 점은 아님)
            return false
        }
    }
}

@Service
class AdvisorTestService {

    fun test() {
        // 1. 기본 Pointcut.TRUE 사용
        val factory = ProxyFactory(AdvisorTargetObj())
        factory.addAdvisor(DefaultPointcutAdvisor(AdviceTestObj())) // DefaultPointcutAdvisor은 기본적으로 Advisor만 넘기면 Pointcut.TRUE를 사용한다

        val proxy = factory.proxy as AdvisorTargetObj
        proxy.test()

        // 2. 직접 Pointcut 생성
        val factory2 = ProxyFactory(AdvisorTargetObj())
        factory2.addAdvisor(DefaultPointcutAdvisor(PointcutTestObj(), AdviceTestObj())) // DefaultPointcutAdvisor은 기본적으로 Advisor만 넘기면 Pointcut.TRUE를 사용한다
        val proxy2 = factory2.proxy as AdvisorTargetObj
        proxy2.test()
    }
}
