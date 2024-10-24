package com.indie.apps.pennypal.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.indie.apps.pennypal.data.database.entity.Budget

@Dao
interface BudgetDao : BaseDao<Budget> {
    @Transaction
    @Query("delete from budget where id = :id")
    suspend fun deleteBudgetFromId(id: Long): Int
}