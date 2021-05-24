package com.gorlah.apisim

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiSimApplication

fun main(args: Array<String>) {
    runApplication<ApiSimApplication>(*args)
}
