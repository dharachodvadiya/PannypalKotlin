package com.indie.apps.pennypal.data.module.balance

import com.indie.apps.pennypal.data.module.CurrencyCountry
import com.indie.apps.pennypal.data.module._interface.ConvertibleAmount

data class Total(
    val totalIncome: Double,
    val totalExpense: Double,
    override val baseCurrencySymbol: String,
    override val baseCurrencyCountryCode: String
) : ConvertibleAmount {
    override fun getAmounts(): List<Double> = listOf(totalIncome, totalExpense)

    override fun withConvertedAmounts(
        amounts: List<Double>,
        baseCurrCode: String,
        baseCurrencySymbol: String
    ): ConvertibleAmount {
        return Total(
            totalIncome = amounts[0],
            totalExpense = amounts[1],
            baseCurrencySymbol = baseCurrencySymbol,
            baseCurrencyCountryCode = baseCurrCode
        )
    }

    override fun toCurrencyCountry(toCode: String) =
        CurrencyCountry(baseCurrencyCountryCode, toCode)
}

//fun Total.toCurrencyCountry(toCode: String) = CurrencyCountry(baseCurrencyCountryCode, toCode)

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