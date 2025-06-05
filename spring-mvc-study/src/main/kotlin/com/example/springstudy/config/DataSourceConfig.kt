package com.example.springstudy.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import javax.sql.DataSource

@Configuration
class DataSourceConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    fun dataSourceProperties() : DataSourceProperties = DataSourceProperties()

    @Primary
    @Bean
    fun dataSource(dataSourceProperties: DataSourceProperties): DataSource = dataSourceProperties.initializeDataSourceBuilder()
        .type(HikariDataSource::class.java)
        .build()

    @Primary
    @Bean
    fun transactionManager(dataSource: DataSource): DataSourceTransactionManager {
        return DataSourceTransactionManager(dataSource)
    }
}
