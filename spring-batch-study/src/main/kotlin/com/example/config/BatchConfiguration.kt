package com.example.config

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.support.transaction.ResourcelessTransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BatchConfiguration {

    /**
     * JobBuilderFactory가 deprecated 된 이유
     * 1. JobBuilderFactory 내부에서 JobRepository를 생성하고 설정하는데, 숨겨져 있어서 인지하기 어려움
     */
    @Bean
    fun testJob(
        jobRepository: JobRepository,
        step: Step
    ) : Job {
        return JobBuilder("test-job", jobRepository)
            .preventRestart()
            .start(step)
            .build()
    }

    @Bean
    fun step(
        jobRepository: JobRepository
    ) : Step {
        return StepBuilder("test-step", jobRepository)
            .tasklet({ contribution, chunkContext ->
                println("Hello, Spring Batch!")
                null
            }, ResourcelessTransactionManager())
            .build()

    }
}
