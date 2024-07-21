package com.indie.apps.pannypal.presentation.ui.common

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
}