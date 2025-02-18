package com.indie.apps.pennypal.data.database.enum

enum class CategoryType(val id: Int) {
    EXPENSE(-1),
    INCOME(1),
    BOTH(0),
}