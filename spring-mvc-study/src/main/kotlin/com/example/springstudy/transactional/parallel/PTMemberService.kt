package com.example.springstudy.transactional.parallel

import com.example.springstudy.transactional.parallel.entity.PTMember
import com.example.springstudy.transactional.parallel.entity.PTMemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.CompletableFuture
import java.util.logging.Logger


/**
 * Coroutine 이름 보기
 * https://stackoverflow.com/questions/54319799/how-to-get-the-name-of-a-coroutine-in-kotlin
 * -Dkotlinx.coroutines.debug 옵션 설정해야 로그에 쓰레드 이름이 보인다
 *
 * Coroutine Dispatcher 관련 설명
 * https://kotlinlang.org/docs/coroutine-context-and-dispatchers.html#children-of-a-coroutine
 * 요약 : CPU 작업은 Default, I/O 작업은 IO
 */

/**
 * 수동 트랜잭션 사용 (TransactionTemplate)
 * https://docs.spring.io/spring-framework/docs/3.0.0.M3/reference/html/ch11s06.html
 */

/**
 * 정리 : Transactional + Async를 사용하려면 가장 바깥 Transaction에 Async가 붙어있어야 할것 같음
 * - 트랜잭션 전파되는 시점에 새로운 쓰레드가 생기면 문제가 발생하는것 같다. 여기서부턴 커스텀의 영역
 */
@Service
class PTMemberService(
    private val ptMemberCommandService : PTMemberCommandService,
    private val ptMemberQueryService: PTMemberQueryService,
) {

    val logger = Logger.getLogger(javaClass.name)

    @set:Autowired
    @set:Lazy
    lateinit var self: PTMemberService

    @Transactional
    fun execution() {
        logger.info("저장 시작")
        val entity = PTMember(name = "kim")
        val saved = ptMemberCommandService.save(entity)
//        saved.get()


        // 이 시점에 롤백이 될걸 기대했는데.. 롤백이 안되넹?
        throw IllegalStateException("ㅜㅜ")
        logger.info("저장 완료")
    }

    @Transactional
    fun executionAsync() {
        logger.info("저장 시작")
        val entity = PTMember(name = "kim")
        val saved = ptMemberCommandService.saveAsync(entity)
        saved.get()
        logger.info("저장 완료")
    }

    @Transactional(readOnly = true)
    fun getSize() {
        val size = ptMemberQueryService.findAll().size
        logger.info("전체 저장 size: $size")
    }
}

@Service
class PTMemberCommandService(
    private val ptMemberRepository: PTMemberRepository,
) {

    @Transactional
    //@Async
    fun save(entity: PTMember): PTMember {
        // Asynchronous를 붙이게 되면, 아예 별도의 트랜잭션으로 잡혀서 롤백이 안된다고 한다
        return ptMemberRepository.save(entity)
    }

    @Transactional
    @Async
    fun saveAsync(entity: PTMember): CompletableFuture<PTMember> {
        return CompletableFuture.supplyAsync {
            ptMemberRepository.save(entity)
        }
    }
}

@Service
class PTMemberQueryService(
    private val ptMemberRepository: PTMemberRepository,
) {

    @Transactional(readOnly = true)
    fun findAll(): List<PTMember> {
        return ptMemberRepository.findAll()
    }
}

@Configuration
@EnableAsync
class AsyncConfig {

    @Bean(name = ["PTAsyncExecutor"])
    fun threadPoolTaskExecutor(): ThreadPoolTaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 10
        executor.maxPoolSize = 10
        executor.setQueueCapacity(1000)
        executor.setThreadNamePrefix("Async-")
        executor.initialize()
        return executor
    }
}
