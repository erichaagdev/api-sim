package com.gorlah.apisim.faker

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.gorlah.apisim.base64.Base64Decoder
import com.gorlah.apisim.gzip.GzipDecompressor
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.onErrorMap
import reactor.kotlin.core.publisher.toMono
import java.time.Duration
import java.util.Random
import kotlin.math.roundToLong

@RestController
class FakerController(
    private val fakerService: FakerService,
    private val base64Decoder: Base64Decoder,
    private val gzipDecompressor: GzipDecompressor,
    private val objectMapper: ObjectMapper,
) {

    private val random = Random()

    @GetMapping
    fun getFakeData(
        @RequestParam(name = "b", required = false) asBase64: String?,
        @RequestParam(name = "g", required = false) asGzip: String?
    ): Mono<JsonNode> =
        Mono.just(parseDefinition(asBase64, asGzip))
            .flatMap { delayIfNecessary(it).thenReturn(it) }
            .flatMap { fakerService.generateFakeData(it) }
            .onErrorMap(UnknownKeywordException::class) { ResponseStatusException(HttpStatus.BAD_REQUEST) }
            .switchIfEmpty(ResponseStatusException(HttpStatus.BAD_REQUEST).toMono())

    private fun delayIfNecessary(fakerDefinition: FakerDefinition): Mono<Void> {
        if (fakerDefinition.delay != null && fakerDefinition.delay > 0) {
            val delay = fakerDefinition.delay
            val delayVariance = maxOf(fakerDefinition.delayVariance ?: 0, 0)
            return Mono.delay(Duration.ofMillis((delay + (random.nextGaussian() * delayVariance)).roundToLong()))
                .then()
        }

        return Mono.empty()
    }

    private fun parseDefinition(asBase64: String?, asGzip: String?): FakerDefinition =
        when {
            asBase64 != null -> objectMapper.readValue(base64Decoder.decode(asBase64))
            asGzip != null -> objectMapper.readValue(gzipDecompressor.decompress(asGzip))
            else -> throw ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Missing query params. Either 'q' or 'g' must be set")
        }
}