package com.gorlah.apifaker.gzip

fun interface GzipDecompressor {

    fun decompress(value: String): String
}