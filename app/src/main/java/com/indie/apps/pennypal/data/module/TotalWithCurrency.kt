package com.indie.apps.pennypal.data.module

data class TotalWithCurrency(
    val totalIncome: Double,
    val totalExpense: Double,
    val currency: String,
)