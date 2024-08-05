package com.indie.apps.pannypal.presentation.ui.common

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

object Util {
    const val SEARCH_NEWS_TIME_DELAY = 500L

    const val PAGE_SIZE = 20
    const val PAGE_PREFETCH_DISTANCE = 20

    const val SAVE_STATE_COUNTRY_CODE = "code"
    const val SAVE_STATE_EDIT_ID = "id"
    const val SAVE_STATE_ADD_EDIT_SUCCESS = "edit"
    const val SAVE_STATE_MERCHANT_NAME_DESC = "merchant_name_desc"
    const val SAVE_STATE_PAYMENT = "payment"

    val TIME_ZONE_OFFSET_IN_MILLI = TimeZone.getDefault().rawOffset

    fun getFormattedStringWithSymbole(value: Double?): String {
        val format = DecimalFormat("##,##,##0.##")
        return format.format(value) + " ₹"
    }

    fun getFormattedString(value: Double?): String {
        val format = java.text.DecimalFormat("##0.##")
        return format.format(value)
    }



    fun isValidPhoneNumber(countryCode: String, phoneNumber: String?): Boolean {
        val phoneUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance()
        try {
            val swissNumberProto: Phonenumber.PhoneNumber = phoneUtil.parse(phoneNumber, countryCode)
            return phoneUtil.isValidNumber(swissNumberProto)
        } catch (e: NumberParseException) {
            System.err.println("NumberParseException was thrown: " + e.toString())
            return false
        }
    }



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