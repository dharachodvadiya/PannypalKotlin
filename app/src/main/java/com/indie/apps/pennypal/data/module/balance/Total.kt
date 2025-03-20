package com.indie.apps.pennypal.data.module.balance

import com.indie.apps.pennypal.data.module.CurrencyCountry

data class Total(
    val totalIncome: Double,
    val totalExpense: Double,
    val baseCurrencySymbol: String,
    val baseCurrencyCountryCode: String
)

fun Total.toCurrencyCountry(toCode: String) = CurrencyCountry(baseCurrencyCountryCode, toCode)