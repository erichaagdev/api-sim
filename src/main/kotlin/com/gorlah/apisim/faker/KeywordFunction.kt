package com.gorlah.apisim.faker

interface KeywordFunction {

    val keyword: String
    fun apply(properties: Map<String, String>): String
}