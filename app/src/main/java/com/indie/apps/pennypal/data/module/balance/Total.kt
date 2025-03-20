package com.indie.apps.pennypal.data.module.balance

import com.indie.apps.pennypal.data.module.CurrencyCountry

data class Total(
    val totalIncome: Double,
    val totalExpense: Double,
    val baseCurrencySymbol: String,
    val baseCurrencyCountryCode: String
)

fun Total.toCurrencyCountry(toCode: String) = CurrencyCountry(baseCurrencyCountryCode, toCode)

fun List<Total>.mergeByAmount(
    baseCurrCode: String,
    baseCurrencySymbol: String
): Total {
    if (this.isEmpty())
        return Total(0.0, 0.0, baseCurrencySymbol, baseCurrCode)
    val firstItem = this.first()
    return firstItem.copy(
        totalIncome = this.sumOf { it.totalIncome },
        totalExpense = this.sumOf { it.totalExpense },
    )
}