package com.gorlah.apifaker.base64

fun interface Base64Encoder {

    fun encode(value: String): String
}