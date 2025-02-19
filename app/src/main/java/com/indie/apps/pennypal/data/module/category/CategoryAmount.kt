package com.indie.apps.pennypal.data.module.category

data class CategoryAmount(
    val id: Long = 0,
    val name: String,
    val amount: Double,
    val type: Int,
    val iconId: Int,
    val iconColorId: Int,
)
