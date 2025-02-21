package com.indie.apps.pennypal.data.module.balance

data class TotalAllTime(
    val totalIncome: Double,
    val totalExpense: Double,
    val currency: String,
)

fun TotalAllTime.toTotalWithCurrency() = TotalWithCurrency(totalIncome, totalExpense, currency)