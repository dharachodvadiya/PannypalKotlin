package com.indie.apps.pennypal.repository

import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class DateConversionRepositoryImpl @Inject constructor() : DateConversionRepository {
    override fun getStartOfDay(timestampMillis: Long, isUtc: Boolean): Long {
        val zoneId = if (isUtc) ZoneOffset.UTC else ZoneId.systemDefault()

        // Get start of the day in the given time zone
        return Instant.ofEpochMilli(timestampMillis)
            .atZone(zoneId)
            .toLocalDate()
            .atStartOfDay(zoneId)
            .toInstant()
            .toEpochMilli()
    }

    override fun getEndOfDay(timestampMillis: Long, isUtc: Boolean): Long {
        val zoneId = if (isUtc) ZoneOffset.UTC else ZoneId.systemDefault()

        // Get end of the day (last moment of the day)
        return Instant.ofEpochMilli(timestampMillis)
            .atZone(zoneId)
            .toLocalDate()
            .plusDays(1)
            .atStartOfDay(zoneId)
            .toInstant()
            .toEpochMilli() - 1 // Subtract 1 millisecond to get the last millisecond of the day
    }

    override fun formatTimestamp(timestampMillis: Long, isUtc: Boolean): String {
        val zoneId = if (isUtc) ZoneOffset.UTC else ZoneId.systemDefault()

        return Instant.ofEpochMilli(timestampMillis)
            .atZone(zoneId)
            .toLocalDate()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

}