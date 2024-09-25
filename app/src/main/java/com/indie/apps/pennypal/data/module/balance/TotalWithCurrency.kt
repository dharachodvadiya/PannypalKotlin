package com.indie.apps.pennypal.data.module.balance

data class TotalWithCurrency(
    val totalIncome: Double,
    val totalExpense: Double,
    val currency: String,
)