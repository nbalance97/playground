package com.example.springstudy.proxy.postprocessor

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

class PostProcessorTest {

    fun test() {
        val ac = AnnotationConfigApplicationContext(innerConfigTest::class.java)

        // 아래 예시에서 beanA가 postProcessor에 의해 B로 대체되었으므로 예외가 발생한다
        // ac.getBean("beanA", A::class.java).test()

        ac.getBean("beanA", B::class.java).test()
    }

    @Configuration
    class innerConfigTest {

        @Bean(name = ["beanA"])
        fun beanA(): A = A()

        @Bean
        fun aPostProcessor(): postProcessor = postProcessor()
    }

    class A {

        val logger = LoggerFactory.getLogger(javaClass)

        fun test() {
            logger.info("testA")
        }
    }

    class B {

        val logger = LoggerFactory.getLogger(javaClass)

        fun test() {
            logger.info("testB")
        }
    }

    class postProcessor: BeanPostProcessor {

        val logger = LoggerFactory.getLogger(javaClass)

        /**
         * bean을 생성한 이후 후처리
         * 여기서 아예 새로운 빈으로 갈아 끼울 수도 있다 -> 프록시로 교체할수도 있음
         */
        override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
            if (bean is A) {
                logger.info("bean은 A클래스이므로 B로 대체")
                return B()
            }

            return bean
        }
    }
}
