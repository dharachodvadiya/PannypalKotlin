package com.indie.apps.pennypal.data.module.balance

data class TotalMonthly(
    val month: String,
    val totalIncome: Double,
    val totalExpense: Double,
)

fun TotalMonthly.toTotalWithCurrency() = Total(totalIncome, totalExpense)