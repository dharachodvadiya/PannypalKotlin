package com.indie.apps.pennypal.data.module

import com.indie.apps.pennypal.data.module._interface.ConvertibleAmount

data class Amount(
    val amount: Double,
    override val baseCurrencySymbol: String,
    override val baseCurrencyCountryCode: String
) : ConvertibleAmount {

    override fun getAmounts(): List<Double> = listOf(amount)

    override fun withConvertedAmounts(amounts: List<Double>, baseCurrCode: String, baseCurrencySymbol: String): ConvertibleAmount {
        return copy(
            amount = amounts[0],
            baseCurrencySymbol = baseCurrencySymbol,
            baseCurrencyCountryCode = baseCurrCode
        )
    }

    override fun toCurrencyCountry(toCode: String) = CurrencyCountry(baseCurrencyCountryCode, toCode)
}

//fun Amount.toCurrencyCountry(toCode: String) = CurrencyCountry(baseCurrencyCountryCode, toCode)

fun List<Amount>.mergeByAmount(
    baseCurrCode: String,
    baseCurrencySymbol: String
): Amount {
    if (this.isEmpty())
        return Amount(0.0, baseCurrencySymbol, baseCurrCode)
    val firstItem = this.first()
    return firstItem.copy(
        amount = this.sumOf { it.amount },
    )
}