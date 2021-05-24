package com.gorlah.apifaker.faker

import com.github.javafaker.Faker
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.UUID
import kotlin.random.Random

@Configuration
class KeywordFunctionConfiguration {

    @Bean
    fun uuidKeywordFunction() = object : KeywordFunction {
        override val keyword = "uuid"
        override fun apply(properties: Map<String, String>) = UUID.randomUUID().toString()
    }

    @Bean
    fun emailKeywordFunction(faker: Faker) = object : KeywordFunction {
        override val keyword = "email"
        override fun apply(properties: Map<String, String>) = faker.internet().emailAddress()
    }

    @Bean
    fun intKeywordFunction() = object : KeywordFunction {
        override val keyword = "int"
        override fun apply(properties: Map<String, String>): String {
            val min = properties["min"]?.toInt() ?: 0
            val max = properties["max"]?.toInt() ?: Int.MAX_VALUE
            return Random.nextInt(min, max).toString()
        }
    }

    @Bean
    fun booleanKeywordFunction() = object : KeywordFunction {
        override val keyword = "boolean"
        override fun apply(properties: Map<String, String>): String {
            return Random.nextBoolean().toString()
        }
    }

    @Bean
    fun firstnameKeywordFunction(faker: Faker) = object : KeywordFunction {
        override val keyword = "firstname"
        override fun apply(properties: Map<String, String>): String {
            return faker.name().firstName()
        }
    }

    @Bean
    fun lastnameKeywordFunction(faker: Faker) = object : KeywordFunction {
        override val keyword = "lastname"
        override fun apply(properties: Map<String, String>): String {
            return faker.name().lastName()
        }
    }
}