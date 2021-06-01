package com.gorlah.apisim.simulation

import com.github.javafaker.Faker
import com.gorlah.apisim.util.DateTimeFormatterType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.TimeZone
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.random.Random

@Configuration
class DataFunctionConfiguration {

    @Bean
    fun uuidDataFunction() = object : DataFunction {
        override val type = "uuid"
        override fun apply(properties: Map<String, String>) = UUID.randomUUID().toString()
    }

    @Bean
    fun emailDataFunction(faker: Faker) = object : DataFunction {
        override val type = "email"
        override fun apply(properties: Map<String, String>) = faker.internet().emailAddress()
    }

    @Bean
    fun intDataFunction() = object : DataFunction {
        override val type = "int"
        override fun apply(properties: Map<String, String>): String {
            val min = properties["min"]?.toInt() ?: 0
            val max = properties["max"]?.toInt() ?: Int.MAX_VALUE
            return Random.nextInt(min, max).toString()
        }
    }

    @Bean
    fun decimalDataFunction() = object : DataFunction {
        override val type = "decimal"
        override fun apply(properties: Map<String, String>): String {
            val min = properties["min"]?.toBigDecimalOrNull() ?: BigDecimal.ZERO
            val max = properties["max"]?.toBigDecimalOrNull() ?: BigDecimal.ONE
            val scale = max(min.scale(), max.scale())
            val range = max - min
            val offset = BigDecimal(Random.nextDouble())
            return (min + (range * offset)).setScale(scale, RoundingMode.HALF_EVEN).toString()
        }
    }

    @Bean
    fun booleanDataFunction() = object : DataFunction {
        override val type = "boolean"
        override fun apply(properties: Map<String, String>): String {
            return Random.nextBoolean().toString()
        }
    }

    @Bean
    fun firstnameDataFunction(faker: Faker) = object : DataFunction {
        override val type = "firstname"
        override fun apply(properties: Map<String, String>): String {
            return faker.name().firstName()
        }
    }

    @Bean
    fun lastnameDataFunction(faker: Faker) = object : DataFunction {
        override val type = "lastname"
        override fun apply(properties: Map<String, String>): String {
            return faker.name().lastName()
        }
    }

    @Bean
    fun zipCodeDataFunction(faker: Faker) = object : DataFunction {
        override val type = "zip-code"
        override fun apply(properties: Map<String, String>): String {
            return faker.address().zipCode()
        }
    }

    @Bean
    fun addressDataFunction(faker: Faker) = object : DataFunction {
        override val type = "address"
        override fun apply(properties: Map<String, String>): String {
            return faker.address().fullAddress()
        }
    }

    @Bean
    fun cityDataFunction(faker: Faker) = object : DataFunction {
        override val type = "city"
        override fun apply(properties: Map<String, String>): String {
            return faker.address().city()
        }
    }

    @Bean
    fun countryDataFunction(faker: Faker) = object : DataFunction {
        override val type = "country"
        override fun apply(properties: Map<String, String>): String {
            return faker.address().country()
        }
    }

    @Bean
    fun countryCodeDataFunction(faker: Faker) = object : DataFunction {
        override val type = "country-code"
        override fun apply(properties: Map<String, String>): String {
            return faker.address().countryCode()
        }
    }

    @Bean
    fun latitudeDataFunction(faker: Faker) = object : DataFunction {
        override val type = "latitude"
        override fun apply(properties: Map<String, String>): String {
            return faker.address().latitude()
        }
    }

    @Bean
    fun longitudeDataFunction(faker: Faker) = object : DataFunction {
        override val type = "longitude"
        override fun apply(properties: Map<String, String>): String {
            return faker.address().longitude()
        }
    }

    @Bean
    fun stateDataFunction(faker: Faker) = object : DataFunction {
        override val type = "state"
        override fun apply(properties: Map<String, String>): String {
            return faker.address().state()
        }
    }

    @Bean
    fun stateCodeDataFunction(faker: Faker) = object : DataFunction {
        override val type = "state-code"
        override fun apply(properties: Map<String, String>): String {
            return faker.address().stateAbbr()
        }
    }

    @Bean
    fun streetAddressDataFunction(faker: Faker) = object : DataFunction {
        override val type = "street-address"
        override fun apply(properties: Map<String, String>): String {
            return faker.address().streetAddress()
        }
    }

    @Bean
    fun animalDataFunction(faker: Faker) = object : DataFunction {
        override val type = "animal"
        override fun apply(properties: Map<String, String>): String {
            return faker.animal().name()
        }
    }

    @Bean
    fun creditCardNumberDataFunction(faker: Faker) = object : DataFunction {
        override val type = "credit-card-number"
        override fun apply(properties: Map<String, String>): String {
            return faker.business().creditCardNumber()
        }
    }

    @Bean
    fun creditCardBrandDataFunction(faker: Faker) = object : DataFunction {
        override val type = "credit-card-brand"
        override fun apply(properties: Map<String, String>): String {
            return faker.business().creditCardType()
        }
    }

    @Bean
    fun asinDataFunction(faker: Faker) = object : DataFunction {
        override val type = "asin"
        override fun apply(properties: Map<String, String>): String {
            return faker.code().asin()
        }
    }

    @Bean
    fun isbn10DataFunction(faker: Faker) = object : DataFunction {
        override val type = "isbn10"
        override fun apply(properties: Map<String, String>): String {
            return faker.code().isbn10()
        }
    }

    @Bean
    fun isbn13DataFunction(faker: Faker) = object : DataFunction {
        override val type = "isbn13"
        override fun apply(properties: Map<String, String>): String {
            return faker.code().isbn13()
        }
    }

    @Bean
    fun colorHexDataFunction(faker: Faker) = object : DataFunction {
        override val type = "color-hex"
        override fun apply(properties: Map<String, String>): String {
            return faker.color().hex()
        }
    }

    @Bean
    fun colorDataFunction(faker: Faker) = object : DataFunction {
        override val type = "color"
        override fun apply(properties: Map<String, String>): String {
            return faker.color().name()
        }
    }

    @Bean
    fun dateFutureDataFunction(faker: Faker) = object : DataFunction {
        override val type = "date-future"
        override fun apply(properties: Map<String, String>): String {
            return generateDate(faker, GenerateDateType.FUTURE, properties)
        }
    }

    @Bean
    fun datePastDataFunction(faker: Faker) = object : DataFunction {
        override val type = "date-past"
        override fun apply(properties: Map<String, String>): String {
            return generateDate(faker, GenerateDateType.PAST, properties)
        }
    }

    private fun generateDate(faker: Faker, type: GenerateDateType, properties: Map<String, String>): String {
        val min = properties["min"]?.toInt() ?: 0
        val max = properties["max"]?.toInt() ?: 3
        val zone = TimeZone.getTimeZone(properties["zone"] ?: "UTC" ).toZoneId()
        val unit = TimeUnit.valueOf(properties["unit"] ?: TimeUnit.DAYS.name)
        val formatter = when {
            properties["formatter"] != null -> DateTimeFormatterType.valueOf(properties["formatter"]!!).formatter
            properties["format"] != null -> DateTimeFormatter.ofPattern(properties["format"]!!)
            else -> DateTimeFormatter.ISO_INSTANT
        }
        return when (type) {
            GenerateDateType.FUTURE -> faker.date().future(max, min, unit).toInstant().atZone(zone).format(formatter)
            GenerateDateType.PAST -> faker.date().past(max, min, unit).toInstant().atZone(zone).format(formatter)
        }
    }

    enum class GenerateDateType {
        FUTURE,
        PAST,
    }

    @Bean
    fun epochMillisDataFunction(faker: Faker) = object : DataFunction {
        override val type = "epoch-millis"
        override fun apply(properties: Map<String, String>): String {
            return Instant.now().toEpochMilli().toString()
        }
    }
}