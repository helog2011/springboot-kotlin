package com.flong.kotlin.modules.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "spring.datasource.druid")
class DataSourceProperties {
    //    @Value("\${spring.datasource.druid.driver-class-name}")
    lateinit var driverClassName: String

    //    @Value("\${spring.datasource.druid.url}")
    lateinit var url: String

    //    @Value("\${spring.datasource.druid.username}")
    lateinit var username: String

    //    @Value("\${spring.datasource.druid.password}")
    lateinit var password: String
    var initialSize: Int = 5
    var minIdle: Int = 5
    var maxActive: Int = 20
    var maxWait: Int = 60000
    var timeBetweenEvictionRunsMillis: Int = 60000
    var minEvictableIdleTimeMillis: Int = 300000
    var validationQuery: String = "SELECT 1 FROM DUAL"
    var testWhileIdle: Boolean = true
    var testOnBorrow: Boolean = false
    var testOnReturn: Boolean = false
    var poolPreparedStatements: Boolean = true
    var maxPoolPreparedStatementPerConnectionSize: Int = 20
    var useGlobalDataSourceStat: Boolean = true
}