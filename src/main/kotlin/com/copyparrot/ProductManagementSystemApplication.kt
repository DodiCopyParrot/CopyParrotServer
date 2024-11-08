package com.copyparrot

import com.dopaminedefense.dodiserver.common.properties.S3Properties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(S3Properties::class)
//@ComponentScan(basePackages = ["com.product.*"])
//@EnableR2dbcRepositories(basePackages = ["com.product.*"])
class ProductManagementSystemApplication

fun main(args: Array<String>) {
    runApplication<ProductManagementSystemApplication>(*args)
}
