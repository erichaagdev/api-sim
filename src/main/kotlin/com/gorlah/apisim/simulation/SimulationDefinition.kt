package com.gorlah.apisim.simulation

data class SimulationDefinition(
    val delay: Delay? = null,
    val properties: Map<String, PropertyDefinition>
) {

    data class Delay(
        val average: Long = 0,
        val variance: Long = 0,
    )
}