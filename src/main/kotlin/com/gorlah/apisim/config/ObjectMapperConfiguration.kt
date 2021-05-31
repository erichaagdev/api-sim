package com.gorlah.apisim.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectMapperConfiguration {

    @Bean
    fun objectMapper() = ObjectMapper().findAndRegisterModules()!!
}