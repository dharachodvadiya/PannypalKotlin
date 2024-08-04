package com.indie.apps.pannypal.data.module

data class MerchantDataDailyTotal(
    val day: String,
    val totalIncome: Double,
    val totalExpense: Double
)