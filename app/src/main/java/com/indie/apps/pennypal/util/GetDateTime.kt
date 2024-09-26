package com.indie.apps.pennypal.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar

internal fun getDateFromMillis(
    millis: Long,
    @SuppressLint("SimpleDateFormat") formatter: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
): String {

    val calendar: Calendar = Calendar.getInstance().apply {
        timeInMillis = millis
    }
    return formatter.format(calendar.time)
}

internal fun getTimeFromMillis(
    millis: Long,
    @SuppressLint("SimpleDateFormat") formatter: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
): String {

    val calendar: Calendar = Calendar.getInstance().apply {
        timeInMillis = millis
    }
    return formatter.format(calendar.time)
}

internal fun getTodayDate(
    @SuppressLint("SimpleDateFormat") formatter: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
): String {
    val calendar: Calendar = Calendar.getInstance()
    return formatter.format(calendar.time)
}

internal fun getYesterdayDate(
    @SuppressLint("SimpleDateFormat") formatter: SimpleDateFormat = SimpleDateFormat(
        "dd-MM-yyyy"
    )
): String {
    val calendar: Calendar = Calendar.getInstance().apply {
        add(Calendar.DATE, -1)
    }
    return formatter.format(calendar.time)
}