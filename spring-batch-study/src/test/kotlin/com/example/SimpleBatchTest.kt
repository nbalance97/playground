
import com.example.SpringBatchStudyApplication
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * @see https://www.baeldung.com/spring-batch-testing-job
 */
@ExtendWith(SpringExtension::class)
@SpringBatchTest
@SpringBootTest(classes = [SpringBatchStudyApplication::class])
class SimpleBatchTest {

    @Autowired
    lateinit var jobLauncherTestUtils: JobLauncherTestUtils

    @Test
    fun test() {
        val jobExecution = jobLauncherTestUtils.launchJob()

        println("status -> ${jobExecution.status}")
        println("instance -> ${jobExecution.jobInstance}")
    }
}
