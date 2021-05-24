package com.gorlah.apifaker.gzip

fun interface GzipCompressor {

    fun compress(value: String): String
}