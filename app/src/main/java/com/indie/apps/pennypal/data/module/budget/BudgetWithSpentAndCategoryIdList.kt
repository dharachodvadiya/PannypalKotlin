package com.indie.apps.pennypal.data.module.budget

import com.indie.apps.pennypal.data.module.category.CategoryAmount

data class BudgetWithSpentAndCategoryIdList(
    val id: Long = 0,

    val title: String,

    val periodType: Int, // Enum: MONTH, YEAR, ONE_TIME

    val startDate: Long,

    val endDate: Long? = null, // Only for one-time budgets

    val budgetAmount: Double,

    val spentAmount: Double,

    val category: List<Long>
)
