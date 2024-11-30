package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.module.budget.BudgetWithCategory
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIdList
import kotlinx.coroutines.flow.Flow

interface BudgetRepository : BaseRepository<BudgetWithCategory> {

    suspend fun deleteBudget(id: Long): Int
    suspend fun insertBudgetWithPeriodValidation(
        obj: BudgetWithCategory, timeZoneOffsetInMilli: Int, year: Int, month: Int
    ): Long

    suspend fun updateBudgetWithPeriodValidation(
        obj: BudgetWithCategory, timeZoneOffsetInMilli: Int, year: Int, month: Int
    ): Int

    fun getBudgetsWithCategoryIdListFromMonth(
        timeZoneOffsetInMilli: Int, year: Int, month: Int
    ): Flow<List<BudgetWithSpentAndCategoryIdList>>

    fun getBudgetWithCategoryFromId(
        budgetId: Long
    ): Flow<BudgetWithCategory>
}