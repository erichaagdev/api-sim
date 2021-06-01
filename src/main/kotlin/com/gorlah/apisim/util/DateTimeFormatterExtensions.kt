package com.gorlah.apisim.util

import java.time.format.DateTimeFormatter

enum class DateTimeFormatterType(val formatter: DateTimeFormatter) {
    BASIC_ISO_DATE(DateTimeFormatter.BASIC_ISO_DATE),
    ISO_DATE(DateTimeFormatter.ISO_DATE),
    ISO_DATE_TIME(DateTimeFormatter.ISO_DATE_TIME),
    ISO_INSTANT(DateTimeFormatter.ISO_INSTANT),
    ISO_LOCAL_DATE(DateTimeFormatter.ISO_LOCAL_DATE),
    ISO_LOCAL_DATE_TIME(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
    ISO_LOCAL_TIME(DateTimeFormatter.ISO_LOCAL_TIME),
    ISO_OFFSET_DATE(DateTimeFormatter.ISO_OFFSET_DATE),
    ISO_OFFSET_DATE_TIME(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
    ISO_OFFSET_TIME(DateTimeFormatter.ISO_OFFSET_TIME),
    ISO_ORDINAL_DATE(DateTimeFormatter.ISO_ORDINAL_DATE),
    ISO_TIME(DateTimeFormatter.ISO_TIME),
    ISO_WEEK_DATE(DateTimeFormatter.ISO_WEEK_DATE),
    ISO_ZONED_DATE_TIME(DateTimeFormatter.ISO_ZONED_DATE_TIME),
    RFC_1123_DATE_TIME(DateTimeFormatter.RFC_1123_DATE_TIME),
}