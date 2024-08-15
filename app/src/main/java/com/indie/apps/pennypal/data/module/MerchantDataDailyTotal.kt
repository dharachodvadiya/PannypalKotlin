package com.indie.apps.pennypal.data.module

data class MerchantDataDailyTotal(
    val day: String,
    val totalIncome: Double,
    val totalExpense: Double
)