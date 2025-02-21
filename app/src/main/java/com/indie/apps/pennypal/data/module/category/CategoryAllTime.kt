package com.indie.apps.pennypal.data.module.category

data class CategoryAllTime(
    val id: Long = 0,
    val name: String,
    val amount: Double,
    val type: Int,
    val iconId: Int,
    val iconColorId: Int
)

fun CategoryAllTime.toCategoryAmount() = CategoryAmount(id, name, amount, type, iconId, iconColorId)
