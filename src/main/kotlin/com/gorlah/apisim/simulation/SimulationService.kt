package com.gorlah.apisim.simulation

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.common.base.Splitter
import com.gorlah.apisim.simulation.PropertyType.AUTO
import com.gorlah.apisim.simulation.PropertyType.BOOLEAN
import com.gorlah.apisim.simulation.PropertyType.NULL
import com.gorlah.apisim.simulation.PropertyType.NUMBER
import com.gorlah.apisim.simulation.PropertyType.STRING
import org.apache.commons.text.StringSubstitutor
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.Random
import java.util.stream.IntStream
import kotlin.math.roundToInt
import kotlin.streams.asSequence

@Service
@Suppress("UnstableApiUsage")
class SimulationService(
    dataFunctions: List<DataFunction>,
    private val simulationDelayService: SimulationDelayService,
    private val objectMapper: ObjectMapper,
) {

    private val keywordFunctions = dataFunctions.associateBy { it.type.trim().lowercase().replace("-", "") }
    private val propertySplitter = Splitter.on(',').omitEmptyStrings().trimResults().withKeyValueSeparator('=')!!
    private val random = Random()
    private val stringSubstitutor = StringSubstitutor({ handleDataFunction(it) }, "#{", "}", '#')

    fun generateSimulatedData(simulationDefinition: SimulationDefinition): Mono<JsonNode> =
        Mono.just(simulationDefinition)
            .flatMap { simulationDelayService.delayIfNecessary(it.delay).thenReturn(it) }
            .map { parseProperties(simulationDefinition.properties) }

    private fun parseProperties(properties: Map<String, PropertyDefinition>): JsonNode =
        properties.entries.fold(objectMapper.createObjectNode()) { response, (name, definition) ->
            generateProperty(response, name, definition)
        }

    private fun generateProperty(response: ObjectNode, name: String, definition: PropertyDefinition): ObjectNode {
        if (definition.properties != null) {
            if (definition.type == PropertyType.ARRAY || definition.count != null) {
                generateArray(
                    response = response,
                    name = name,
                    countAverage = definition.count?.average ?: 1,
                    countVariance = definition.count?.variance ?: 0,
                    properties = definition.properties
                )
            } else {
                generateObject(
                    response = response,
                    name = name,
                    properties = definition.properties
                )
            }
        } else if (definition.value != null) {
            generateValue(
                response = response,
                name = name,
                expression = definition.value,
                type = definition.type,
            )
        }

        return response
    }

    private fun generateArray(
        response: ObjectNode,
        name: String,
        countAverage: Int,
        countVariance: Int,
        properties: Map<String, PropertyDefinition>
    ): JsonNode =
        IntStream.range(0, maxOf((countAverage + (random.nextGaussian() * countVariance)).roundToInt(), 0))
            .asSequence()
            .fold(response.withArray(name)) { array, _ ->
                array.add(parseProperties(properties))
            }

    private fun generateObject(
        response: ObjectNode,
        name: String,
        properties: Map<String, PropertyDefinition>
    ): JsonNode =
        response.set(name, parseProperties(properties))

    private fun generateValue(
        response: ObjectNode,
        name: String,
        expression: String,
        type: PropertyType?
    ): JsonNode {
        val value = stringSubstitutor.replace(expression)
        when (type) {
            AUTO -> convertAuto(response, name, value)
            BOOLEAN -> convertBoolean(response, name, value)
            NULL -> response.putNull(name)
            NUMBER -> convertNumber(response, name, value)
            STRING -> response.put(name, value)
            else -> response.put(name, value)
        }
        return response
    }

    private fun convertAuto(response: ObjectNode, name: String, value: String): JsonNode =
        when (value) {
            "true" -> response.put(name, true)
            "false" -> response.put(name, false)
            "null" -> response.putNull(name)
            else -> convertNumber(response, name, value)
        }

    private fun convertNumber(response: ObjectNode, name: String, value: String): JsonNode =
        value.toBigDecimalOrNull()
            .let {
                if (it == null) response.put(name, value) else response.put(name, it)
            }

    private fun convertBoolean(response: ObjectNode, name: String, value: String): JsonNode =
        when (value) {
            "true" -> response.put(name, true)
            "false" -> response.put(name, false)
            else -> convertAuto(response, name, value)
        }

    private fun handleDataFunction(expression: String): String {
        val split = expression.split(":", limit = 2).toTypedArray()
        val type = split[0].trim().lowercase().replace("-", "")
        val properties = if (split.size == 1) emptyMap() else propertySplitter.split(split[1])
        return keywordFunctions[type]?.apply(properties) ?: throw UnknownTypeException(type)
    }
}