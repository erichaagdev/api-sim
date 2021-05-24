package com.gorlah.apisim.gzip

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.util.Base64
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

@Configuration
class GzipConfiguration {

    @Bean
    fun gzipDecompressor() = GzipDecompressor { value -> decompress(Base64.getUrlDecoder().decode(value)) }

    @Bean
    fun gzipCompressor() = GzipCompressor { value -> Base64.getUrlEncoder().encodeToString(compress(value)) }

    fun decompress(data: ByteArray): String =
        ByteArrayInputStream(data).use { inputStream ->
            GZIPInputStream(inputStream).use { gzip ->
                InputStreamReader(gzip).use { streamReader ->
                    BufferedReader(streamReader).use { bufferedReader ->
                        bufferedReader.readLines().joinToString()
                    }
                }
            }
        }

    fun compress(data: String): ByteArray =
        ByteArrayOutputStream().use { outputStream ->
            GZIPOutputStream(outputStream).use { gzip ->
                gzip.write(data.toByteArray())
            }

            outputStream.toByteArray()
        }
}