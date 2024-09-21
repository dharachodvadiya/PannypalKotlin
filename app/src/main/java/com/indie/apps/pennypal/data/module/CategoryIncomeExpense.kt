package com.indie.apps.pennypal.data.module

data class CategoryIncomeExpense(
    val id: Long = 0,
    val name: String,
    val month: String,
    val totalIncome: Double,
    val totalExpense: Double
)
