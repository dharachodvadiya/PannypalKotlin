package com.indie.apps.pennypal.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.indie.apps.pennypal.data.database.db_entity.BudgetCategory

@Dao
interface BudgetCategoryDao : BaseDao<BudgetCategory> {
    @Insert
    suspend fun insertBudgetCategoryList(budgetCategories: List<BudgetCategory>): List<Long>

    @Transaction
    @Query("delete from budget_category where budget_id = :budgetId")
    suspend fun deleteBudgetCategoryFromBudgetId(budgetId: Long): Int
}