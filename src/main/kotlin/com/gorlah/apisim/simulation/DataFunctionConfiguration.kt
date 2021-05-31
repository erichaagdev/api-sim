package com.gorlah.apisim.simulation

import com.github.javafaker.Faker
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.UUID
import kotlin.random.Random

@Configuration
class DataFunctionConfiguration {

    @Bean
    fun uuidDataFunction() = object : DataFunction {
        override val type = "uuid"
        override fun apply(properties: Map<String, String>) = UUID.randomUUID().toString()
    }

    @Bean
    fun emailDataFunction(faker: Faker) = object : DataFunction {
        override val type = "email"
        override fun apply(properties: Map<String, String>) = faker.internet().emailAddress()
    }

    @Bean
    fun intDataFunction() = object : DataFunction {
        override val type = "int"
        override fun apply(properties: Map<String, String>): String {
            val min = properties["min"]?.toInt() ?: 0
            val max = properties["max"]?.toInt() ?: Int.MAX_VALUE
            return Random.nextInt(min, max).toString()
        }
    }

    @Bean
    fun booleanDataFunction() = object : DataFunction {
        override val type = "boolean"
        override fun apply(properties: Map<String, String>): String {
            return Random.nextBoolean().toString()
        }
    }

    @Bean
    fun firstnameDataFunction(faker: Faker) = object : DataFunction {
        override val type = "firstname"
        override fun apply(properties: Map<String, String>): String {
            return faker.name().firstName()
        }
    }

    @Bean
    fun lastnameDataFunction(faker: Faker) = object : DataFunction {
        override val type = "lastname"
        override fun apply(properties: Map<String, String>): String {
            return faker.name().lastName()
        }
    }
}