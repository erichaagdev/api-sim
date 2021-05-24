package com.gorlah.apifaker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiFakerApplication

fun main(args: Array<String>) {
    runApplication<ApiFakerApplication>(*args)
}
