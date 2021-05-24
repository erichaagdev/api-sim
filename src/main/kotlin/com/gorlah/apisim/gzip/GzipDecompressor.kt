package com.gorlah.apisim.gzip

fun interface GzipDecompressor {

    fun decompress(value: String): String
}