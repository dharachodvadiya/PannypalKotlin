package com.indie.apps.pennypal.repository

interface DateConversionRepository {
    fun getStartOfDay(timestampMillis: Long, isUtc: Boolean = false): Long
    fun getEndOfDay(timestampMillis: Long, isUtc: Boolean = false): Long
    fun formatTimestamp(timestampMillis: Long, isUtc: Boolean = false): String
}