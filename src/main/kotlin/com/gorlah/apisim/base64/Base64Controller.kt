package com.gorlah.apisim.base64

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.gorlah.apisim.util.APPLICATION_JSON
import com.gorlah.apisim.util.TEXT_PLAIN
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
        consumes = [APPLICATION_JSON],
        produces = [TEXT_PLAIN],
    )
    fun base64Encode(@RequestBody request: JsonNode): Mono<String> =
        Mono.just(request)
            .map { objectMapper.writeValueAsString(it) }
            .map { base64Encoder.encode(it) }

    @PostMapping(
        path = ["/base64decode"],
        consumes = [TEXT_PLAIN],
        produces = [TEXT_PLAIN],
    )
    fun base64Decode(@RequestBody request: String): Mono<String> =
        Mono.just(request)
            .map { base64Decoder.decode(it) }
}