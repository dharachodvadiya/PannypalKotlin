package com.indie.apps.pennypal.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.DisplayMetrics
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

object Util {
    var currentCurrencySymbol = "$"
    const val SEARCH_NEWS_TIME_DELAY = 500L

    const val LIST_ITEM_ANIM_DELAY = 700L
    const val EDIT_ITEM_ANIM_TIME = 400
    const val ADD_ITEM_ANIM_TIME = 55

    const val PAGE_SIZE = 20
    const val PAGE_PREFETCH_DISTANCE = 20

    const val SAVE_STATE_COUNTRY_DIAL_CODE = "dial_code"
    const val SAVE_STATE_CURRENCY_CODE = "currency_code"
    const val SAVE_STATE_EDIT_ID = "edit_id"
    const val SAVE_STATE_ADD_SUCCESS = "add_success"
    const val SAVE_STATE_EDIT_SUCCESS = "edit_success"
    const val SAVE_STATE_ADD_EDIT_SUCCESS_ID = "edit_add_id"
    const val SAVE_STATE_MERCHANT_NAME_DESC = "merchant_name_desc"
    const val SAVE_STATE_PAYMENT = "payment"
    const val SAVE_STATE_MERCHANT_DATA_ADD_EDIT_ID = "merchant_data_add_edit_id"
    const val SAVE_STATE_MERCHANT_ADD_EDIT_ID = "merchant_add_edit_id"
    const val SAVE_STATE_MERCHANT_DATA_ADD_SUCCESS = "merchant_add_success"
    const val SAVE_STATE_MERCHANT_DATA_EDIT_SUCCESS = "merchant_edit_success"
    const val SAVE_STATE_MERCHANT_LOCK = "merchant_lock"
    const val SAVE_STATE_SHOW_CURRENCY = "is_show_currency"
    const val PARAM_MERCHANT_ID = "merchant_id"
    const val PARAM_EDIT_MERCHANT_DATA_ID = "edit_merchant_data_id"

    val TIME_ZONE_OFFSET_IN_MILLI = TimeZone.getDefault().rawOffset

    fun getFormattedStringWithSymbol(value: Double?, symbol: String = currentCurrencySymbol): String {
        val format = DecimalFormat("##,##,##0.##")
        return "$symbol " + format.format(value)
    }

    fun getFormattedString(value: Double?): String {
        val format = DecimalFormat("##0.##")
        return format.format(value)
    }


    fun isValidPhoneNumber(countryCode: String, phoneNumber: String?): Boolean {
        val phoneUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance()
        try {
            val swissNumberProto: Phonenumber.PhoneNumber =
                phoneUtil.parse(phoneNumber, countryCode)
            return phoneUtil.isValidNumber(swissNumberProto)
        } catch (e: NumberParseException) {
            System.err.println("NumberParseException was thrown: $e")
            return false
        }
    }

    fun getDateFromMillis(
        timeInMillis: Long,
        @SuppressLint("SimpleDateFormat") formatter: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
    ): String {

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        return formatter.format(calendar.time)
    }

    fun getTodayDate(@SuppressLint("SimpleDateFormat") formatter: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")): String {
        val calendar: Calendar = Calendar.getInstance()
        return formatter.format(calendar.time)
    }

    fun getYesterdayDate(@SuppressLint("SimpleDateFormat") formatter: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        return formatter.format(calendar.time)
    }

    fun Float.pxToDp(context: Context): Float =
        (this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))
}