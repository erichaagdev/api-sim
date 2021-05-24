package com.gorlah.apisim.gzip

fun interface GzipCompressor {

    fun compress(value: String): String
}