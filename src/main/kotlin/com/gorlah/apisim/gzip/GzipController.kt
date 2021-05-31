package com.gorlah.apisim.gzip

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.gorlah.apisim.util.APPLICATION_JSON
import com.gorlah.apisim.util.TEXT_PLAIN
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
        consumes = [APPLICATION_JSON],
        produces = [TEXT_PLAIN],
    )
    fun base64Encode(@RequestBody request: JsonNode): Mono<String> =
        Mono.just(request)
            .map { objectMapper.writeValueAsString(it) }
            .map { gzipCompressor.compress(it) }

    @PostMapping(
        path = ["/gzipDecompress"],
        consumes = [TEXT_PLAIN],
        produces = [TEXT_PLAIN],
    )
    fun base64Decode(@RequestBody request: String): Mono<String> =
        Mono.just(request)
            .map { gzipDecompressor.decompress(it) }
}