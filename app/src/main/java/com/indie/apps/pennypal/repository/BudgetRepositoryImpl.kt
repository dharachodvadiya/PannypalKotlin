package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.dao.BudgetCategoryDao
import com.indie.apps.pennypal.data.database.dao.BudgetDao
import com.indie.apps.pennypal.data.module.budget.BudgetWithCategory
import com.indie.apps.pennypal.data.module.budget.toBudget
import com.indie.apps.pennypal.data.module.budget.toBudgetCategoryList
import com.indie.apps.pennypal.data.module.budget.toBudgetWithSpentAndCategoryIdList
import kotlinx.coroutines.flow.map
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

    override fun getBudgetsWithCategoryIdListFromMonth(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int
    ) = budgetDao.getBudgetsWithCategoryIdListFromMonth(
        timeZoneOffsetInMilli = timeZoneOffsetInMilli,
        year = year.toString(),
        monthPlusOne = (month + 1).toString()
    ).map {
        it.map { budget ->
            budget.toBudgetWithSpentAndCategoryIdList()
        }
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