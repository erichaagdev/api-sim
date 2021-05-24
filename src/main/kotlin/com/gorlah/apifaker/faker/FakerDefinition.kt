package com.gorlah.apifaker.faker

data class FakerDefinition(
    val delay: Long? = null,
    val delayVariance: Long? = null,
    val properties: Map<String, PropertyDefinition>
)