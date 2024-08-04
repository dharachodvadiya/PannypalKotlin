package com.indie.apps.pannypal.util

import androidx.core.text.util.LocalePreferences.FirstDayOfWeek.Days
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

object Constant {
    const val QUERY_PAGE_SIZE = 20
    val TIME_ZONE_OFFSET_IN_MILLI = TimeZone.getDefault().rawOffset

    fun getTodayDate(): String {

        val formatter = SimpleDateFormat("dd-MM-yyyy")

        val calendar: Calendar = Calendar.getInstance()
        return formatter.format(calendar.time)
    }

    fun getYesterdayDate(): String {

        val formatter = SimpleDateFormat("dd-MM-yyyy")

        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        return formatter.format(calendar.time)
    }

}