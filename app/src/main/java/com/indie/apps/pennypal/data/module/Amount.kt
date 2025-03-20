package com.indie.apps.pennypal.data.module

import com.indie.apps.pennypal.data.module.balance.Total
import com.indie.apps.pennypal.data.module.balance.mergeByAmount

data class Amount(
    val amount: Double,
    val baseCurrencySymbol: String,
    val baseCurrencyCountryCode: String
)

fun Amount.toCurrencyCountry(toCode: String) = CurrencyCountry(baseCurrencyCountryCode, toCode)

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