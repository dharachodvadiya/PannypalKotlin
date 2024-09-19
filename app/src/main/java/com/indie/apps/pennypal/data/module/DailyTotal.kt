package com.indie.apps.pennypal.data.module

data class DailyTotal(
    val day: String,
    val totalIncome: Double,
    val totalExpense: Double
)