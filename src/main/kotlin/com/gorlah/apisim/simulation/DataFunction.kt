package com.gorlah.apisim.simulation

interface DataFunction {

    val type: String
    fun apply(properties: Map<String, String>): String
}