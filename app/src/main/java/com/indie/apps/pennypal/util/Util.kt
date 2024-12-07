package com.indie.apps.pennypal.util

import android.content.Context
import android.util.DisplayMetrics
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import com.indie.apps.pennypal.data.module.ContactNumberAndCode
import java.text.DecimalFormat
import java.util.TimeZone

object Util {
    var isSubscribed = false

    const val DB_NAME = "pennypal_money_db"
    const val DB_BACKUP_NAME = "pennypal_money_backup"

    var currentCurrencySymbol = "$"
    const val SEARCH_NEWS_TIME_DELAY = 500L

    const val LIST_ITEM_ANIM_DELAY = 700L
    const val EDIT_ITEM_ANIM_TIME = 400
    const val ADD_ITEM_ANIM_TIME = 55

    const val PAGE_SIZE = 20
    const val PAGE_PREFETCH_DISTANCE = 20

    const val SAVE_STATE_SAVABLE_DIALOG = "savable"
    const val SAVE_STATE_SELECT_COUNTRY_CODE = "selected_country_code"

    const val SAVE_STATE_COUNTRY_DIAL_CODE = "dial_code"
    const val SAVE_STATE_COUNTRY_CODE = "country_code"

    const val SAVE_STATE_PAYMENT_EDIT_ID = "payment_edit_id"
    const val SAVE_STATE_SELECT_PAYMENT_ID = "select_payment_id"
    const val SAVE_STATE_PAYMENT_EDIT_SUCCESS = "payment_edit_success"
    const val SAVE_STATE_PAYMENT_ADD_EDIT_ID = "payment_add_edit_id"

    const val SAVE_STATE_CATEGORY = "category"
    const val SAVE_STATE_SELECT_CATEGORY_ID = "select_category_id"
    const val SAVE_STATE_SELECT_CATEGORY_ID_LIST = "select_category_id_list"
    const val SAVE_STATE_CATEGORY_TYPE = "category_type"

    const val SAVE_STATE_MERCHANT_EDIT_ID = "merchant_edit_id"
    const val SAVE_STATE_ADD_MERCHANT_SUCCESS = "merchant_add_success"
    const val SAVE_STATE_EDIT_MERCHANT_SUCCESS = "merchant_edit_success"
    const val SAVE_STATE_ADD_EDIT_MERCHANT_SUCCESS_ID = "merchant_edit_add_id"

    const val SAVE_STATE_MERCHANT_NAME_DESC = "merchant_name_desc"

    const val SAVE_STATE_CONTACT_NUMBER_DIAL_CODE = "contact_number_code"

    const val SAVE_STATE_PAYMENT = "payment"
    const val SAVE_STATE_MERCHANT_DATA_ADD_EDIT_ID = "merchant_data_add_edit_id"
    const val SAVE_STATE_MERCHANT_ADD_EDIT_ID = "merchant_add_edit_id"
    const val SAVE_STATE_MERCHANT_DATA_ADD_SUCCESS = "merchant_data_add_success"
    const val SAVE_STATE_MERCHANT_DATA_EDIT_SUCCESS = "merchant_data_edit_success"

    const val SAVE_STATE_MERCHANT_LOCK = "merchant_lock"

    const val SAVE_STATE_SHOW_CURRENCY = "is_show_currency"

    const val SAVE_STATE_PERIOD_TYPE = "period_type"
    const val SAVE_STATE_BUDGET_FILTER_ID = "budget_filter_id"

    const val PARAM_MERCHANT_ID = "merchant_id"
    const val PARAM_EDIT_MERCHANT_DATA_ID = "edit_merchant_data_id"
    const val PARAM_BUDGET_ID = "budget_id"


    const val PREF_BALANCE_VIEW = "overview_balance_view"


    const val REQUEST_CODE_GOOGLE_SIGN_IN = 101

    val TIME_ZONE_OFFSET_IN_MILLI = TimeZone.getDefault().rawOffset

    fun getFormattedStringWithSymbol(
        value: Double?,
        symbol: String = currentCurrencySymbol
    ): String {
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

    fun getContactNumberAndCodeFromPhoneNumber(phoneNumber: String): ContactNumberAndCode {
        val phoneNumberUtil = PhoneNumberUtil.getInstance()
        return try {
            // Parse the phone number with the default region set to empty
            val number: Phonenumber.PhoneNumber = phoneNumberUtil.parse(phoneNumber, null)
            //"+${number.countryCode}"

            ContactNumberAndCode(
                dialCode = "+${number.countryCode}",
                phoneNumber = phoneNumber.replace("+${number.countryCode}", ""),
            )
            //phoneNumberUtil.getRegionCodeForNumber(number)
        } catch (e: Exception) {
            // Handle parsing errors
            e.printStackTrace()
            ContactNumberAndCode(
                dialCode = null,
                phoneNumber = phoneNumber
            )
        }
    }

    /*fun getDateFromMillis(
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
    }*/

    fun Float.pxToDp(context: Context): Float =
        (this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))
}