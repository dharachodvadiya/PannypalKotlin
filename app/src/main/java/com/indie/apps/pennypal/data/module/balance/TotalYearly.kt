package com.indie.apps.pennypal.data.module.balance

data class TotalYearly(
    val year: String,
    val totalIncome: Double,
    val totalExpense: Double,
)

fun TotalYearly.toTotalWithCurrency() = Total(totalIncome, totalExpense)