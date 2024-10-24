package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.module.BudgetWithCategory

interface BudgetRepository : BaseRepository<BudgetWithCategory> {

    suspend fun deleteBudget(id: Long): Int
}