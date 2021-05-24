package com.gorlah.apisim.faker

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.common.base.Splitter
import org.apache.commons.text.StringSubstitutor
import org.springframework.core.convert.ConversionException
import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.Random
import java.util.stream.IntStream
import kotlin.math.roundToInt
import kotlin.streams.asSequence

@Service
@Suppress("UnstableApiUsage")
class FakerService(
    keywordFunctions: List<KeywordFunction>,
    private val conversionService: ConversionService,
    private val objectMapper: ObjectMapper,
) {

    private val keywordFunctions = keywordFunctions.associateBy { it.keyword.lowercase() }
    private val propertySplitter = Splitter.on(',').omitEmptyStrings().trimResults().withKeyValueSeparator('=')!!
    private val random = Random()
    private val stringSubstitutor = StringSubstitutor({ handleKeyword(it) }, "#{", "}", '#')

    fun generateFakeData(fakerDefinition: FakerDefinition): Mono<JsonNode> =
        parseProperties(fakerDefinition.properties).toMono()

    private fun parseProperties(properties: Map<String, PropertyDefinition>): JsonNode =
        properties.entries.fold(objectMapper.createObjectNode()) { response, (name, definition) ->
            handleProperty(response, name, definition)
        }

    private fun handleProperty(response: ObjectNode, name: String, definition: PropertyDefinition): ObjectNode {
        if (definition.properties != null) {
            if (definition.type != null && definition.type == PropertyType.ARRAY) {
                handleArray(
                    response = response,
                    name = name,
                    count = definition.count ?: 1,
                    countVariance = definition.countVariance ?: 0,
                    properties = definition.properties
                )
            } else {
                handleObject(
                    response = response,
                    name = name,
                    properties = definition.properties
                )
            }
        } else if (definition.value != null) {
            handleValue(
                response = response,
                name = name,
                expression = definition.value,
                type = definition.type,
            )
        }

        return response
    }

    private fun handleArray(
        response: ObjectNode,
        name: String,
        count: Int,
        countVariance: Int,
        properties: Map<String, PropertyDefinition>
    ): JsonNode =
        IntStream.range(0, maxOf((count + (random.nextGaussian() * countVariance)).roundToInt(), 0))
            .asSequence()
            .fold(response.withArray(name)) { array, _ ->
                array.add(parseProperties(properties))
            }

    private fun handleObject(
        response: ObjectNode,
        name: String,
        properties: Map<String, PropertyDefinition>
    ): JsonNode =
        response.set(name, parseProperties(properties))

    private fun handleValue(
        response: ObjectNode,
        name: String,
        expression: String,
        type: PropertyType?
    ): JsonNode {
        val value = stringSubstitutor.replace(expression)
        when (type) {
            PropertyType.BOOLEAN -> convertBoolean(response, name, value)
            PropertyType.NULL -> response.putNull(name)
            PropertyType.NUMBER -> convertInt(response, name, value)
            PropertyType.STRING -> response.put(name, value)
            null -> convertValue(response, name, value)
            else -> response.put(name, value)
        }
        return response
    }

    private fun convertValue(response: ObjectNode, name: String, value: String): JsonNode =
        when (value) {
            "true" -> response.put(name, true)
            "false" -> response.put(name, false)
            "null" -> response.putNull(name)
            else -> convertInt(response, name, value)
        }

    private fun convertInt(response: ObjectNode, name: String, value: String): JsonNode =
        try {
            response.put(name, conversionService.convert(value, Int::class.java))
        } catch (_: ConversionException) {
            response.put(name, value)
        }

    private fun convertBoolean(response: ObjectNode, name: String, value: String): JsonNode =
        when (value) {
            "true" -> response.put(name, true)
            "false" -> response.put(name, false)
            else -> convertValue(response, name, value)
        }

    private fun handleKeyword(expression: String): String {
        val split = expression.split(":", limit = 2).toTypedArray()
        val keyword = split[0].trim().lowercase()
        val properties = if (split.size == 1) emptyMap() else propertySplitter.split(split[1])
        return keywordFunctions[keyword]?.apply(properties) ?: throw UnknownKeywordException(keyword)
    }
}