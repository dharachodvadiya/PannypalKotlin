package com.indie.apps.pennypal.data.module.budget

import com.indie.apps.pennypal.data.module.category.CategoryAmount

data class BudgetWithCategoryResult(
    val budgetId: Long,
    val title: String,
    val budgetAmount: Double,
    val periodType: Int,
    val startDate: Long,
    val endDate: Long?,
    val createdDate: Long,
    val categoryId: Long,
    val categoryAmount: Double
)

fun List<BudgetWithCategoryResult>.toBudgetWithCategories(): BudgetWithCategory {
    val firstResult = this.first()

    // Map the flat list of category data into BudgetCategory objects
    val categoryList = this.map {
        CategoryAmount(
            id = it.categoryId,
            amount = it.categoryAmount,
            name = "",
            type = 0
        )
    }

    return firstResult.run {
        BudgetWithCategory(
            id = budgetId,
            title = title,
            amount = budgetAmount,
            periodType = periodType,
            startDate = startDate,
            endDate = endDate,
            createdDate = createdDate,
            category = categoryList
        )
    }
}