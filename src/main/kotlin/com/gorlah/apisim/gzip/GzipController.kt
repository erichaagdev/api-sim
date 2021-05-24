package com.gorlah.apisim.gzip

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class GzipController(
    private val gzipCompressor: GzipCompressor,
    private val gzipDecompressor: GzipDecompressor,
    private val objectMapper: ObjectMapper,
) {

    @PostMapping(
        path = ["/gzipCompress"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.TEXT_PLAIN_VALUE],
    )
    fun base64Encode(@RequestBody request: JsonNode): Mono<String> =
        Mono.just(request)
            .map { objectMapper.writeValueAsString(it) }
            .map { gzipCompressor.compress(it) }

    @PostMapping(
        path = ["/gzipDecompress"],
        consumes = [MediaType.TEXT_PLAIN_VALUE],
        produces = [MediaType.TEXT_PLAIN_VALUE],
    )
    fun base64Decode(@RequestBody request: String): Mono<String> =
        Mono.just(request)
            .map { gzipDecompressor.decompress(it) }
}