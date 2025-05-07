package com.indie.apps.pennypal.data.module.budget

import com.indie.apps.pennypal.data.database.db_entity.Budget
import com.indie.apps.pennypal.data.database.db_entity.BudgetCategory
import com.indie.apps.pennypal.data.module.category.CategoryAmount

data class BudgetWithCategory(
    val id: Long = 0,

    val title: String,

    val amount: Double,

    val periodType: Int, // Enum: MONTH, YEAR, ONE_TIME

    val startDate: Long,

    val endDate: Long? = null, // Only for one-time budgets

    val createdDate: Long,

    val category: List<CategoryAmount>,

    val originalCurrencyId: Long,

    val originalAmountSymbol: String,

    )

fun BudgetWithCategory.toBudget() =
    Budget(id, title, amount, periodType, startDate, endDate, createdDate, originalCurrencyId)

fun BudgetWithCategory.toBudgetCategoryList(): List<BudgetCategory> {
    val list = mutableListOf<BudgetCategory>()

    this.category.forEach { item ->
        list.add(BudgetCategory(0, id, item.id, item.amount))
    }
    return list
}
