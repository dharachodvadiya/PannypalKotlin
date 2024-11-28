package com.indie.apps.pennypal.data.module.budget

data class BudgetWithSpentAndCategoryIds(
    val id: Long = 0,

    val title: String,

    val periodType: Int, // Enum: MONTH, YEAR, ONE_TIME

    val startDate: Long,

    val endDate: Long? = null, // Only for one-time budgets

    val budgetAmount: Double,

    val spentAmount: Double = 0.0,

    val categoryIds: String?,
)

fun BudgetWithSpentAndCategoryIds.toBudgetWithSpentAndCategoryIdList(): BudgetWithSpentAndCategoryIdList {
    return BudgetWithSpentAndCategoryIdList(
        id = id,
        title = title,
        periodType = periodType,
        startDate = startDate,
        endDate = endDate,
        budgetAmount = budgetAmount,
        spentAmount = spentAmount,
        category = categoryIds?.split(",")
            ?.filter { it.isNotBlank() } // Ensure that there are no blank entries
            ?.map { it.toLong() }
            ?: emptyList())
}

