package com.indie.apps.pennypal.data.module._interface

import com.indie.apps.pennypal.data.module.CurrencyCountry

interface ConvertibleAmount {
    val baseCurrencyCountryCode: String
    val baseCurrencySymbol: String

    // Amounts to convert (single for CategoryAmount/Amount, dual for Total)
    fun getAmounts(): List<Double>

    // Create a new instance with converted amounts and updated currency info
    fun withConvertedAmounts(
        amounts: List<Double>,
        baseCurrCode: String,
        baseCurrencySymbol: String
    ): ConvertibleAmount

    // Helper to get the currency country code for exchange rate conversion
    fun toCurrencyCountry(toCode: String): CurrencyCountry
}