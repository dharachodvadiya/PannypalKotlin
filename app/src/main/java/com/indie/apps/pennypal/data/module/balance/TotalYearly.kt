package com.indie.apps.pennypal.data.module.balance

data class TotalYearly(
    val year: String,
    val totalIncome: Double,
    val totalExpense: Double,
    val currency: String,
)

fun TotalYearly.toTotalWithCurrency() = TotalWithCurrency(totalIncome, totalExpense, currency)