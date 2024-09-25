package com.indie.apps.pennypal.data.module.balance

data class TotalMonthly(
    val month: String,
    val totalIncome: Double,
    val totalExpense: Double,
    val currency: String,
)

fun TotalMonthly.toTotalWithCurrency() = TotalWithCurrency(totalIncome, totalExpense, currency)