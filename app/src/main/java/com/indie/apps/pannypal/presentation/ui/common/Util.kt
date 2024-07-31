package com.indie.apps.pannypal.presentation.ui.common

import android.util.Log
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import java.text.DecimalFormat

object Util {

    fun getFormattedStringWithSymbole(value: Double?): String {
        val format = DecimalFormat("##,##,##0.##")
        return format.format(value) + " ₹"
    }

    fun getFormattedString(value: Double?): String {
        val format = java.text.DecimalFormat("##0.##")
        return format.format(value)
    }

    const val countryCode = "code"

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
}