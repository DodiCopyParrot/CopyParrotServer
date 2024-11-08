package com.copyparrot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
//@ComponentScan(basePackages = ["com.product.*"])
//@EnableR2dbcRepositories(basePackages = ["com.product.*"])
class ProductManagementSystemApplication

fun main(args: Array<String>) {
    runApplication<ProductManagementSystemApplication>(*args)
}
