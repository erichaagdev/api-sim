package com.gorlah.apisim.config

import com.github.javafaker.Faker
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FakerConfiguration {

    @Bean
    fun faker() = Faker()
}