package com.indie.apps.pennypal.data.module.balance

data class TotalDaily(
    val day: String,
    val totalIncome: Double,
    val totalExpense: Double
)