package com.indie.apps.pennypal.data.module

data class CategoryAmount(
    val id: Long = 0,
    val name: String,
    val month: String,
    val amount: Double,
    val type : Int
)
