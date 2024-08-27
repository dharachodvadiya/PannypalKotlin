package com.indie.apps.cpp.data.utils

import com.indie.apps.cpp.data.getCountryData

fun getSymbolFromCurrencyCode(currencyCode: String): String {
    return getCountryData().first {
        it.currencyCode.lowercase() == currencyCode.lowercase()
    }.currencySymbol
}