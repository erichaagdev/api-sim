package com.gorlah.apisim.simulation

data class PropertyDefinition(
    val count: Count? = null,
    val properties: Map<String, PropertyDefinition>? = null,
    val type: PropertyType = PropertyType.AUTO,
    val value: String? = null,
) {

    data class Count(
        val average: Int = 0,
        val variance: Int = 0,
    )
}