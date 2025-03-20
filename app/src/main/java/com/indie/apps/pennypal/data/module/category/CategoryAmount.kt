package com.indie.apps.pennypal.data.module.category

import com.indie.apps.pennypal.data.module.CurrencyCountry

data class CategoryAmount(
    val id: Long = 0,
    val name: String,
    val amount: Double,
    val type: Int,
    val iconId: Int,
    val iconColorId: Int,
    val baseCurrencySymbol: String = "",
    val baseCurrencyCountryCode: String = ""
)

fun CategoryAmount.toCurrencyCountry(toCode: String) =
    CurrencyCountry(baseCurrencyCountryCode, toCode)

