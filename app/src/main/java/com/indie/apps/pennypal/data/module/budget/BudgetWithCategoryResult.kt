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
    val categoryAmount: Double,
    val categoryName: String,
    val categoryType: Int,
    val categoryIconId: Int,
    val categoryIconColorId: Int
)

fun List<BudgetWithCategoryResult>.toBudgetWithCategories(): BudgetWithCategory {
    val firstResult = this.first()

    // Map the flat list of category data into BudgetCategory objects
    val categoryList = this.map {
        CategoryAmount(
            id = it.categoryId,
            amount = it.categoryAmount,
            name = it.categoryName,
            type = it.categoryType,
            iconId = it.categoryIconId,
            iconColorId = it.categoryIconColorId
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