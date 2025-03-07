package com.indie.apps.pennypal.repository

import androidx.paging.PagingSource
import com.indie.apps.pennypal.data.database.dao.BudgetCategoryDao
import com.indie.apps.pennypal.data.database.dao.BudgetDao
import com.indie.apps.pennypal.data.database.dao.MerchantDataDao
import com.indie.apps.pennypal.data.database.enum.PeriodType
import com.indie.apps.pennypal.data.module.budget.BudgetWithCategory
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIdList
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIds
import com.indie.apps.pennypal.data.module.budget.toBudget
import com.indie.apps.pennypal.data.module.budget.toBudgetCategoryList
import com.indie.apps.pennypal.data.module.budget.toBudgetWithCategories
import com.indie.apps.pennypal.data.module.budget.toBudgetWithSpentAndCategoryIdList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao,
    private val budgetCategoryDao: BudgetCategoryDao,
    private val merchantDataDao: MerchantDataDao,
    private val dispatcher: CoroutineDispatcher
) : BudgetRepository {
    override suspend fun deleteBudget(id: Long): Int {
        val count = budgetDao.deleteBudgetFromId(id)
        budgetCategoryDao.deleteBudgetCategoryFromBudgetId(id)
        return count
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
        return if (count == 0) insert(obj) else -1

    }

    override suspend fun updateBudgetWithPeriodValidation(
        obj: BudgetWithCategory,
        timeZoneOffsetInMilli: Int,
        year: Int,
        month: Int
    ): Int {
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
        return if (budgetList.isNotEmpty() && budgetList.firstOrNull()?.id == obj.id) {
            update(obj)
        } else if (budgetList.isEmpty()) {
            update(obj)
        } else {
            0
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
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
    }.flatMapConcat { budgets ->  // Sequentially process each budget
        flow {
            val budgetWithSpentList = mutableListOf<BudgetWithSpentAndCategoryIdList>()

            // Process each budget one by one
            for (budget in budgets) {

                when (budget.periodType) {
                    PeriodType.MONTH.id -> {
                        val startCal: Calendar =
                            Calendar.getInstance().apply { timeInMillis = budget.startDate }

                        val amountMonth = merchantDataDao.getTotalAmountForMonthAndCategory(
                            timeZoneOffsetInMilli = timeZoneOffsetInMilli,
                            year = startCal.get(Calendar.YEAR).toString(),
                            monthPlusOne = (startCal.get(Calendar.MONTH) + 1).toString(),
                            categoryIds = budget.category
                        )
                        budgetWithSpentList.add(
                            budget.copy(
                                spentAmount = amountMonth
                            )
                        )
                    }

                    PeriodType.YEAR.id -> {
                        val startCal: Calendar =
                            Calendar.getInstance().apply { timeInMillis = budget.startDate }
                        val amountYear = merchantDataDao.getTotalAmountForYearAndCategory(
                            year = startCal.get(Calendar.YEAR).toString(),
                            categoryIds = budget.category,
                            timeZoneOffsetInMilli = timeZoneOffsetInMilli
                        )
                        budgetWithSpentList.add(
                            budget.copy(
                                spentAmount = amountYear
                            )
                        )
                    }

                    PeriodType.ONE_TIME.id -> {
                        val amount = merchantDataDao.getTotalAmountForBetweenDatesAndCategory(
                            startTime = budget.startDate,
                            endTime = budget.endDate ?: 0,
                            categoryIds = budget.category
                        )

                        budgetWithSpentList.add(
                            budget.copy(
                                spentAmount = amount
                            )
                        )
                    }

                }
            }
            // Emit the list once all items are processed
            emit(budgetWithSpentList)
        }
    }.flowOn(dispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
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
    }.flatMapConcat { budgets ->  // Sequentially process each budget
        flow {
            val budgetWithSpentList = mutableListOf<BudgetWithSpentAndCategoryIdList>()

            // Process each budget one by one
            for (budget in budgets) {

                when (budget.periodType) {
                    PeriodType.MONTH.id -> {
                        val startCal: Calendar =
                            Calendar.getInstance().apply { timeInMillis = budget.startDate }

                        val amountMonth = merchantDataDao.getTotalAmountForMonthAndCategory(
                            timeZoneOffsetInMilli = timeZoneOffsetInMilli,
                            year = startCal.get(Calendar.YEAR).toString(),
                            monthPlusOne = (startCal.get(Calendar.MONTH) + 1).toString(),
                            categoryIds = budget.category
                        )
                        budgetWithSpentList.add(
                            budget.copy(
                                spentAmount = amountMonth
                            )
                        )
                    }

                    PeriodType.YEAR.id -> {
                        val startCal: Calendar =
                            Calendar.getInstance().apply { timeInMillis = budget.startDate }
                        val amountYear = merchantDataDao.getTotalAmountForYearAndCategory(
                            year = startCal.get(Calendar.YEAR).toString(),
                            categoryIds = budget.category,
                            timeZoneOffsetInMilli = timeZoneOffsetInMilli
                        )
                        budgetWithSpentList.add(
                            budget.copy(
                                spentAmount = amountYear
                            )
                        )
                    }

                    PeriodType.ONE_TIME.id -> {
                        val amount = merchantDataDao.getTotalAmountForBetweenDatesAndCategory(
                            startTime = budget.startDate,
                            endTime = budget.endDate ?: 0,
                            categoryIds = budget.category
                        )

                        budgetWithSpentList.add(
                            budget.copy(
                                spentAmount = amount
                            )
                        )
                    }

                }
            }
            // Emit the list once all items are processed
            emit(budgetWithSpentList)
        }
    }.flowOn(dispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
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
    }.flatMapConcat { budgets ->  // Sequentially process each budget
        flow {
            val budgetWithSpentList = mutableListOf<BudgetWithSpentAndCategoryIdList>()

            // Process each budget one by one
            for (budget in budgets) {

                when (budget.periodType) {
                    PeriodType.MONTH.id -> {
                        val startCal: Calendar =
                            Calendar.getInstance().apply { timeInMillis = budget.startDate }

                        val amountMonth = merchantDataDao.getTotalAmountForMonthAndCategory(
                            timeZoneOffsetInMilli = timeZoneOffsetInMilli,
                            year = startCal.get(Calendar.YEAR).toString(),
                            monthPlusOne = (startCal.get(Calendar.MONTH) + 1).toString(),
                            categoryIds = budget.category
                        )
                        budgetWithSpentList.add(
                            budget.copy(
                                spentAmount = amountMonth
                            )
                        )
                    }

                    PeriodType.YEAR.id -> {
                        val startCal: Calendar =
                            Calendar.getInstance().apply { timeInMillis = budget.startDate }
                        val amountYear = merchantDataDao.getTotalAmountForYearAndCategory(
                            year = startCal.get(Calendar.YEAR).toString(),
                            categoryIds = budget.category,
                            timeZoneOffsetInMilli = timeZoneOffsetInMilli
                        )
                        budgetWithSpentList.add(
                            budget.copy(
                                spentAmount = amountYear
                            )
                        )
                    }

                    PeriodType.ONE_TIME.id -> {
                        val amount = merchantDataDao.getTotalAmountForBetweenDatesAndCategory(
                            startTime = budget.startDate,
                            endTime = budget.endDate ?: 0,
                            categoryIds = budget.category
                        )

                        budgetWithSpentList.add(
                            budget.copy(
                                spentAmount = amount
                            )
                        )
                    }

                }
            }
            // Emit the list once all items are processed
            emit(budgetWithSpentList)
        }
    }.flowOn(dispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
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
    }.flatMapConcat { budgets ->  // Sequentially process each budget
        flow {
            val budgetWithSpentList = mutableListOf<BudgetWithSpentAndCategoryIdList>()

            // Process each budget one by one
            for (budget in budgets) {

                when (budget.periodType) {
                    PeriodType.MONTH.id -> {
                        val startCal: Calendar =
                            Calendar.getInstance().apply { timeInMillis = budget.startDate }

                        val amountMonth = merchantDataDao.getTotalAmountForMonthAndCategory(
                            timeZoneOffsetInMilli = timeZoneOffsetInMilli,
                            year = startCal.get(Calendar.YEAR).toString(),
                            monthPlusOne = (startCal.get(Calendar.MONTH) + 1).toString(),
                            categoryIds = budget.category
                        )
                        budgetWithSpentList.add(
                            budget.copy(
                                spentAmount = amountMonth
                            )
                        )
                    }

                    PeriodType.YEAR.id -> {
                        val startCal: Calendar =
                            Calendar.getInstance().apply { timeInMillis = budget.startDate }
                        val amountYear = merchantDataDao.getTotalAmountForYearAndCategory(
                            year = startCal.get(Calendar.YEAR).toString(),
                            categoryIds = budget.category,
                            timeZoneOffsetInMilli = timeZoneOffsetInMilli
                        )
                        budgetWithSpentList.add(
                            budget.copy(
                                spentAmount = amountYear
                            )
                        )
                    }

                    PeriodType.ONE_TIME.id -> {
                        val amount = merchantDataDao.getTotalAmountForBetweenDatesAndCategory(
                            startTime = budget.startDate,
                            endTime = budget.endDate ?: 0,
                            categoryIds = budget.category
                        )

                        budgetWithSpentList.add(
                            budget.copy(
                                spentAmount = amount
                            )
                        )
                    }

                }
            }
            // Emit the list once all items are processed
            emit(budgetWithSpentList)
        }
    }.flowOn(dispatcher)

    override suspend fun update(obj: BudgetWithCategory): Int {
        val updateCount = budgetDao.update(obj.toBudget())
        budgetCategoryDao.deleteBudgetCategoryFromBudgetId(obj.id)
        budgetCategoryDao.insertBudgetCategoryList(obj.toBudgetCategoryList())
        return updateCount
    }


}