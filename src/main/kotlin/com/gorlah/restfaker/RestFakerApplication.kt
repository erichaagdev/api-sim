package com.gorlah.restfaker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RestFakerApplication

fun main(args: Array<String>) {
    runApplication<RestFakerApplication>(*args)
}
