package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.entity.Budget
import com.indie.apps.pennypal.data.module.budget.BudgetWithCategory
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIdList
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIds
import kotlinx.coroutines.flow.Flow

interface BudgetRepository : BaseRepository<BudgetWithCategory> {

    suspend fun deleteBudget(id: Long): Int

    fun getBudgetsWithCategoryIdListFromMonth(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int
    ): Flow<List<BudgetWithSpentAndCategoryIdList>>
}