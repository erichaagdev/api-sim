package com.gorlah.apisim.simulation

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

@RestController
class SimulationController(
    private val simulationService: SimulationService,
    private val base64Decoder: Base64Decoder,
    private val gzipDecompressor: GzipDecompressor,
    private val objectMapper: ObjectMapper,
) {

    @GetMapping
    fun getSimulatedData(
        @RequestParam(name = "b", required = false) asBase64: String?,
        @RequestParam(name = "g", required = false) asGzip: String?
    ): Mono<JsonNode> =
        Mono.just(parseDefinition(asBase64, asGzip))
            .flatMap { simulationService.generateSimulatedData(it) }
            .onErrorMap(UnknownTypeException::class) { ResponseStatusException(HttpStatus.BAD_REQUEST) }
            .switchIfEmpty(ResponseStatusException(HttpStatus.BAD_REQUEST).toMono())

    private fun parseDefinition(asBase64: String?, asGzip: String?): SimulationDefinition =
        when {
            asBase64 != null -> objectMapper.readValue(base64Decoder.decode(asBase64))
            asGzip != null -> objectMapper.readValue(gzipDecompressor.decompress(asGzip))
            else -> throw ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Missing query params. Either 'q' or 'g' must be set")
        }
}