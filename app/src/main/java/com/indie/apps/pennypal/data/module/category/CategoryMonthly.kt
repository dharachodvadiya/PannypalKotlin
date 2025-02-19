package com.indie.apps.pennypal.data.module.category

data class CategoryMonthly(
    val id: Long = 0,
    val name: String,
    val month: String,
    val amount: Double,
    val type: Int,
    val iconId: Int,
    val iconColorId: Int
)

fun CategoryMonthly.toCategoryAmount() = CategoryAmount(id, name, amount, type, iconId, iconColorId)
