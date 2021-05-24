package com.gorlah.apisim.base64

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Base64

@Configuration
class Base64Configuration {

    @Bean
    fun base64Decoder() = Base64Decoder { Base64.getUrlDecoder().decode(it).decodeToString() }

    @Bean
    fun base64Encoder() = Base64Encoder { Base64.getUrlEncoder().encodeToString(it.toByteArray()) }
}