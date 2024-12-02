package com.indie.apps.pennypal.repository

import androidx.paging.PagingSource
import com.indie.apps.pennypal.data.module.budget.BudgetWithCategory
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIdList
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIds
import kotlinx.coroutines.flow.Flow

interface BudgetRepository : BaseRepository<BudgetWithCategory> {

    suspend fun deleteBudget(id: Long): Int
    suspend fun insertBudgetWithPeriodValidation(
        obj: BudgetWithCategory, timeZoneOffsetInMilli: Int, year: Int, month: Int
    ): Long

    suspend fun updateBudgetWithPeriodValidation(
        obj: BudgetWithCategory, timeZoneOffsetInMilli: Int, year: Int, month: Int
    ): Int

    fun getBudgetsAndSpentWithCategoryIdListFromMonth(
        timeZoneOffsetInMilli: Int, year: Int, month: Int
    ): Flow<List<BudgetWithSpentAndCategoryIdList>>

    fun getBudgetWithCategoryFromId(
        budgetId: Long
    ): Flow<BudgetWithCategory>

    fun getPastBudgetsWithCategoryIdListFromPeriodType(
        timeZoneOffsetInMilli: Int, year: Int, month: Int, periodType: Int
    ): PagingSource<Int, BudgetWithSpentAndCategoryIds>

    fun getUpcomingBudgetsWithCategoryIdListFromPeriodType(
        timeZoneOffsetInMilli: Int, year: Int, month: Int, periodType: Int
    ): PagingSource<Int, BudgetWithSpentAndCategoryIds>
}