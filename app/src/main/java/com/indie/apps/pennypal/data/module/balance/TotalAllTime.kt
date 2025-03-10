package com.indie.apps.pennypal.data.module.balance

data class TotalAllTime(
    val totalIncome: Double,
    val totalExpense: Double,
)

fun TotalAllTime.toTotalWithCurrency() = Total(totalIncome, totalExpense)