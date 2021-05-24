package com.gorlah.apisim.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectMapperConfiguration {

    @Bean
    fun objectMapper() = jacksonObjectMapper().findAndRegisterModules()!!
}