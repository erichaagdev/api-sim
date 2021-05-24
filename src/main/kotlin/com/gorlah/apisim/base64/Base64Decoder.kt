package com.gorlah.apisim.base64

fun interface Base64Decoder {

    fun decode(value: String): String
}