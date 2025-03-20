package com.indie.apps.pennypal.repository

import androidx.paging.PagingSource
import com.indie.apps.pennypal.data.database.dao.BudgetCategoryDao
import com.indie.apps.pennypal.data.database.dao.BudgetDao
import com.indie.apps.pennypal.data.database.enum.PeriodType
import com.indie.apps.pennypal.data.module.budget.BudgetWithCategory
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIds
import com.indie.apps.pennypal.data.module.budget.toBudget
import com.indie.apps.pennypal.data.module.budget.toBudgetCategoryList
import com.indie.apps.pennypal.data.module.budget.toBudgetWithCategories
import com.indie.apps.pennypal.data.module.budget.toBudgetWithSpentAndCategoryIdList
import com.indie.apps.pennypal.extension.mapBudgetsWithSpent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao,
    private val budgetCategoryDao: BudgetCategoryDao,
    private val merchantDataRepository: MerchantDataRepository,
    private val dispatcher: CoroutineDispatcher
) : BudgetRepository {
    override suspend fun deleteBudget(id: Long) = withContext(dispatcher) {
        val count = budgetDao.deleteBudgetFromId(id)
        budgetCategoryDao.deleteBudgetCategoryFromBudgetId(id)
        count
    }

    override fun getBudgetWithCategoryFromId(budgetId: Long) =
        budgetDao.getBudgetWithCategoryFromId(budgetId).map { it.toBudgetWithCategories() }
            .flowOn(dispatcher)

    override fun getPastBudgetsWithCategoryIdListFromPeriodType(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int,
        periodType: Int
    ): PagingSource<Int, BudgetWithSpentAndCategoryIds> {
        return budgetDao.getPastBudgetsWithCategoryIdListFromPeriodType(
            timeZoneOffsetInMilli = timeZoneOffsetInMilli,
            year = year.toString(),
            monthPlusOne = (month + 1).toString(),
            periodType = periodType
        )
    }

    override fun getUpcomingBudgetsWithCategoryIdListFromPeriodType(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int,
        periodType: Int
    ): PagingSource<Int, BudgetWithSpentAndCategoryIds> {
        return budgetDao.getUpComingBudgetsWithCategoryIdListFromPeriodType(
            timeZoneOffsetInMilli = timeZoneOffsetInMilli,
            year = year.toString(),
            monthPlusOne = (month + 1).toString(),
            periodType = periodType
        )
    }

    override suspend fun insert(obj: BudgetWithCategory) = withContext(dispatcher) {
        val id = budgetDao.insert(obj.toBudget())
        budgetCategoryDao.insertBudgetCategoryList(obj.copy(id = id).toBudgetCategoryList())
        id
    }

    override suspend fun insertBudgetWithPeriodValidation(
        obj: BudgetWithCategory,
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int
    ) = withContext(dispatcher) {
        val count = when (obj.periodType) {
            PeriodType.MONTH.id -> {
                val res = budgetDao.getBudgetDataFromMonth(
                    monthPlusOne = (month + 1).toString(),
                    year = year.toString(),
                    timeZoneOffsetInMilli = timeZoneOffsetInMilli
                ).first()
                if (res.isNotEmpty()) res.count() else 0
            }

            PeriodType.YEAR.id -> {
                val res = budgetDao.getBudgetDataFromYear(
                    year = year.toString(),
                    timeZoneOffsetInMilli = timeZoneOffsetInMilli
                ).first()
                if (res.isNotEmpty()) res.count() else 0
            }

            else -> 0
        }
        if (count == 0) insert(obj) else -1

    }

    override suspend fun updateBudgetWithPeriodValidation(
        obj: BudgetWithCategory,
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int
    ) = withContext(dispatcher) {
        val budgetList = when (obj.periodType) {
            PeriodType.MONTH.id -> {
                budgetDao.getBudgetDataFromMonth(
                    monthPlusOne = (month + 1).toString(),
                    year = year.toString(),
                    timeZoneOffsetInMilli = timeZoneOffsetInMilli
                ).first()

            }

            PeriodType.YEAR.id -> {
                budgetDao.getBudgetDataFromYear(
                    year = year.toString(),
                    timeZoneOffsetInMilli = timeZoneOffsetInMilli
                ).first()
            }

            else -> emptyList()
        }
        if (budgetList.isNotEmpty() && budgetList.firstOrNull()?.id == obj.id) {
            update(obj)
        } else if (budgetList.isEmpty()) {
            update(obj)
        } else {
            0
        }
    }

    override fun getBudgetsAndSpentWithCategoryIdListFromMonth(
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
    }.mapBudgetsWithSpent(timeZoneOffsetInMilli, merchantDataRepository).flowOn(dispatcher)

    override fun getOneTimeBudgetsWithCategoryIdListFromMonth(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int
    ) = budgetDao.getOneTimeBudgetsWithCategoryIdListFromMonth(
        timeZoneOffsetInMilli = timeZoneOffsetInMilli,
        year = year.toString(),
        monthPlusOne = (month + 1).toString()
    ).map {
        it.map { budget ->
            budget.toBudgetWithSpentAndCategoryIdList()
        }
    }.mapBudgetsWithSpent(timeZoneOffsetInMilli, merchantDataRepository).flowOn(dispatcher)

    override fun getMonthBudgetsAndSpentWithCategoryIdListFromMonth(
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int
    ) = budgetDao.getMonthBudgetsWithCategoryIdListFromMonth(
        timeZoneOffsetInMilli = timeZoneOffsetInMilli,
        year = year.toString(),
        monthPlusOne = (month + 1).toString()
    ).map {
        it.map { budget ->
            budget.toBudgetWithSpentAndCategoryIdList()
        }
    }.mapBudgetsWithSpent(timeZoneOffsetInMilli, merchantDataRepository).flowOn(dispatcher)

    override fun getYearBudgetsAndSpentWithCategoryIdListFromYear(
        timeZoneOffsetInMilli: Int,
        year: Int
    ) = budgetDao.getYearBudgetsWithCategoryIdListFromYear(
        timeZoneOffsetInMilli = timeZoneOffsetInMilli,
        year = year.toString(),
    ).map {
        it.map { budget ->
            budget.toBudgetWithSpentAndCategoryIdList()
        }
    }.mapBudgetsWithSpent(timeZoneOffsetInMilli, merchantDataRepository).flowOn(dispatcher)

    override suspend fun update(obj: BudgetWithCategory) = withContext(dispatcher) {
        val updateCount = budgetDao.update(obj.toBudget())
        budgetCategoryDao.deleteBudgetCategoryFromBudgetId(obj.id)
        budgetCategoryDao.insertBudgetCategoryList(obj.toBudgetCategoryList())
        updateCount
    }
}
