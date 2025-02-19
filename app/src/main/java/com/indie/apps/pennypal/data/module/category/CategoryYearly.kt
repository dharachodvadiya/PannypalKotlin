package com.indie.apps.pennypal.data.module.category

data class CategoryYearly(
    val id: Long = 0,
    val name: String,
    val year: String,
    val amount: Double,
    val type: Int,
    val iconId: Int,
    val iconColorId: Int
)

fun CategoryYearly.toCategoryAmount() = CategoryAmount(id, name, amount, type, iconId, iconColorId)
