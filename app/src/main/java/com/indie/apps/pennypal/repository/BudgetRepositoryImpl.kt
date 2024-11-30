package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.dao.BudgetCategoryDao
import com.indie.apps.pennypal.data.database.dao.BudgetDao
import com.indie.apps.pennypal.data.database.entity.Budget
import com.indie.apps.pennypal.data.database.enum.BudgetPeriodType
import com.indie.apps.pennypal.data.module.budget.BudgetWithCategory
import com.indie.apps.pennypal.data.module.budget.BudgetWithCategoryResult
import com.indie.apps.pennypal.data.module.budget.toBudget
import com.indie.apps.pennypal.data.module.budget.toBudgetCategoryList
import com.indie.apps.pennypal.data.module.budget.toBudgetWithCategories
import com.indie.apps.pennypal.data.module.budget.toBudgetWithSpentAndCategoryIdList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
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

    override fun getBudgetWithCategoryFromId(budgetId: Long) =
        budgetDao.getBudgetWithCategoryFromId(budgetId).map { it.toBudgetWithCategories() }

    override suspend fun insert(obj: BudgetWithCategory): Long {
        val id = budgetDao.insert(obj.toBudget())
        budgetCategoryDao.insertBudgetCategoryList(obj.copy(id = id).toBudgetCategoryList())
        return id
    }

    override suspend fun insertBudgetWithPeriodValidation(
        obj: BudgetWithCategory,
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int
    ): Long {
        val count = when (obj.periodType) {
            BudgetPeriodType.MONTH.id -> {
                val res = budgetDao.getBudgetDataFromMonth(
                    monthPlusOne = (month + 1).toString(),
                    year = year.toString(),
                    timeZoneOffsetInMilli = timeZoneOffsetInMilli
                ).first()

                if (res.isNotEmpty()) res.count() else 0
            }

            BudgetPeriodType.YEAR.id -> {
                val res = budgetDao.getBudgetDataFromYear(
                    year = year.toString(),
                    timeZoneOffsetInMilli = timeZoneOffsetInMilli
                ).first()
                if (res.isNotEmpty()) res.count() else 0
            }

            else -> 0
        }
        return if (count == 0) insert(obj) else -1

    }

    override suspend fun updateBudgetWithPeriodValidation(
        obj: BudgetWithCategory,
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int
    ): Int {
        val budgetList = when (obj.periodType) {
            BudgetPeriodType.MONTH.id -> {
                budgetDao.getBudgetDataFromMonth(
                    monthPlusOne = (month + 1).toString(),
                    year = year.toString(),
                    timeZoneOffsetInMilli = timeZoneOffsetInMilli
                ).first()

            }

            BudgetPeriodType.YEAR.id -> {
                budgetDao.getBudgetDataFromYear(
                    year = year.toString(),
                    timeZoneOffsetInMilli = timeZoneOffsetInMilli
                ).first()
            }

            else -> emptyList()
        }
        return if (budgetList.isNotEmpty() && budgetList.first().id == obj.id) {
            update(obj)
        } else if (budgetList.isEmpty()) {
            update(obj)
        } else {
            0
        }
    }

    override suspend fun update(obj: BudgetWithCategory): Int {
        val updateCount = budgetDao.update(obj.toBudget())
        budgetCategoryDao.deleteBudgetCategoryFromBudgetId(obj.id)
        budgetCategoryDao.insertBudgetCategoryList(obj.toBudgetCategoryList())
        return updateCount
    }


}