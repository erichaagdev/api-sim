package com.gorlah.apifaker.faker

data class PropertyDefinition(
    val count: Int? = null,
    val countVariance: Int? = null,
    val properties: Map<String, PropertyDefinition>? = null,
    val type: PropertyType? = null,
    val value: String? = null,
)