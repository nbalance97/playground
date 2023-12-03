package com.example.springstudy.mvc.transactional.parallel

import com.example.springstudy.mvc.transactional.parallel.entity.PTMember
import com.example.springstudy.mvc.transactional.parallel.entity.PTMemberRepository
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.logging.Logger
import kotlin.random.Random

@Service
class PTMemberService(
    private val ptMemberRepository: PTMemberRepository
) {

    val logger = Logger.getLogger(this::javaClass.name)

    @set:Autowired
    @set:Lazy
    lateinit var self: PTMemberService

    @Transactional
    fun execution() {
        logger.info("전체 저장 시작")

        /**
         * Coroutine 이름 보기
         * https://stackoverflow.com/questions/54319799/how-to-get-the-name-of-a-coroutine-in-kotlin
         * -Dkotlinx.coroutines.debug 옵션 설정해야 로그에 쓰레드 이름이 보인다
         */
        runBlocking {
            val members = (1..200)
                .map { async (CoroutineName("coroutine-thread-$it")) { self!!.save(it) }}
                .map { it -> it.await() }

            println(members)
        }

        logger.info("전체 저장 완료")
    }

    @Transactional(readOnly = true)
    fun getSize() {
        val size = ptMemberRepository.findAll().size

        logger.info("전체 저장 size: $size")
    }

    @Transactional
    suspend fun save(num: Int) : PTMember {
        logger.info("[${Thread.currentThread().name}][$num] 저장 진행중")
        val entity = PTMember(name = "kim $num")
        val saved = ptMemberRepository.save(entity)
        delay(Random.nextLong(0, 500))

        if (num == 159) {
            throw RuntimeException("159번째는 에러인데.. 전체 롤백이 될까?")
        }

        logger.info("[${Thread.currentThread().name}][$num] 저장 완료")
        return saved
    }
}
