package com.indie.apps.pennypal.data.module

data class YearlyTotal(
    val year: String,
    val totalIncome: Double,
    val totalExpense: Double,
    val currency: String,
)