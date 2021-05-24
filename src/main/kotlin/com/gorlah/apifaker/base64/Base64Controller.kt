package com.gorlah.apifaker.base64

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class Base64Controller(
    private val base64Decoder: Base64Decoder,
    private val base64Encoder: Base64Encoder,
    private val objectMapper: ObjectMapper,
) {

    @PostMapping(
        path = ["/base64encode"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.TEXT_PLAIN_VALUE],
    )
    fun base64Encode(@RequestBody request: JsonNode): Mono<String> =
        Mono.just(request)
            .map { objectMapper.writeValueAsString(it) }
            .map { base64Encoder.encode(it) }

    @PostMapping(
        path = ["/base64decode"],
        consumes = [MediaType.TEXT_PLAIN_VALUE],
        produces = [MediaType.TEXT_PLAIN_VALUE],
    )
    fun base64Decode(@RequestBody request: String): Mono<String> =
        Mono.just(request)
            .map { base64Decoder.decode(it) }
}