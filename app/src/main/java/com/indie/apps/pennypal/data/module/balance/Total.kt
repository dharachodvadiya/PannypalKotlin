package com.indie.apps.pennypal.data.module.balance

data class Total(
    val totalIncome: Double,
    val totalExpense: Double,
    val baseCurrencySymbol: String,
    val baseCurrencyCountryCode: String
)