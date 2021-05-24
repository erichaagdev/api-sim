package com.gorlah.apifaker.faker

interface KeywordFunction {

    val keyword: String
    fun apply(properties: Map<String, String>): String
}