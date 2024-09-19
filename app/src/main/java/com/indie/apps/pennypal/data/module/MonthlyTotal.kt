package com.indie.apps.pennypal.data.module

import com.indie.apps.pennypal.data.entity.Merchant

data class MonthlyTotal(
    val month: String,
    val totalIncome: Double,
    val totalExpense: Double,
    val currency: String,
)

fun MonthlyTotal.toTotalWithCurrency() = TotalWithCurrency(totalIncome, totalExpense, currency)