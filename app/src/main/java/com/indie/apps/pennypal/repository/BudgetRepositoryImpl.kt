package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.dao.BudgetCategoryDao
import com.indie.apps.pennypal.data.database.dao.BudgetDao
import com.indie.apps.pennypal.data.module.BudgetWithCategory
import com.indie.apps.pennypal.data.module.toBudget
import com.indie.apps.pennypal.data.module.toBudgetCategoryList
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao,
    private val budgetCategoryDao: BudgetCategoryDao,
) : BudgetRepository {
    override suspend fun deleteBudget(id: Long): Int {
        val count = budgetDao.deleteBudgetFromId(id)
        budgetCategoryDao.deleteBudgetCategoryFromBudgetId(id)
        return count
    }

    override suspend fun insert(obj: BudgetWithCategory): Long {
        val id = budgetDao.insert(obj.toBudget())
        budgetCategoryDao.insertBudgetCategoryList(obj.copy(id = id).toBudgetCategoryList())
        return id
    }

    override suspend fun update(obj: BudgetWithCategory): Int {
        val updateCount = budgetDao.update(obj.toBudget())
        budgetCategoryDao.deleteBudgetCategoryFromBudgetId(obj.id)
        budgetCategoryDao.insertBudgetCategoryList(obj.toBudgetCategoryList())
        return updateCount
    }


}