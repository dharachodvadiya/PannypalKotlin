package com.indie.apps.pennypal.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.indie.apps.pennypal.data.database.enum.PeriodType
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIdList
import com.indie.apps.pennypal.data.module.budget.toBudgetWithSpentAndCategoryIdList
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.BudgetRepository
import com.indie.apps.pennypal.repository.MerchantDataRepository
import com.indie.apps.pennypal.util.Util
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

class GetUpComingBudgetsAndSpentWithCategoryIdListFromPeriodType @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val merchantDataRepository: MerchantDataRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadData(
        year: Int, month: Int, periodType: Int
    ): Flow<PagingData<BudgetWithSpentAndCategoryIdList>> {

        return Pager(
            PagingConfig(
                pageSize = Util.PAGE_SIZE, prefetchDistance = Util.PAGE_PREFETCH_DISTANCE
            )
        ) {
            budgetRepository.getUpcomingBudgetsWithCategoryIdListFromPeriodType(
                year = year,
                month = month,
                periodType = periodType,
                timeZoneOffsetInMilli = Util.TIME_ZONE_OFFSET_IN_MILLI
            )
        }
            .flow
            .map { it.map { budget -> budget.toBudgetWithSpentAndCategoryIdList() } }
            .flatMapConcat { budgets ->
                flow {
                    // Loop over each budget and fetch the spent amount based on the periodType
                    val bd = budgets.map { budget ->
                        when (budget.periodType) {
                            PeriodType.MONTH.id -> {
                                val startCal: Calendar =
                                    Calendar.getInstance().apply { timeInMillis = budget.startDate }
                                val amountMonth =
                                    merchantDataRepository.getTotalAmountForMonthAndCategory(
                                        timeZoneOffsetInMilli = Util.TIME_ZONE_OFFSET_IN_MILLI,
                                        year = startCal.get(Calendar.YEAR),
                                        month = startCal.get(Calendar.MONTH),
                                        categoryIds = budget.category
                                    )

                                // Return the budget with updated spent amount
                                budget.copy(spentAmount = amountMonth)
                            }

                            PeriodType.YEAR.id -> {
                                val startCal: Calendar =
                                    Calendar.getInstance().apply { timeInMillis = budget.startDate }
                                val amountYear =
                                    merchantDataRepository.getTotalAmountForYearAndCategory(
                                        year = startCal.get(Calendar.YEAR),
                                        categoryIds = budget.category,
                                        timeZoneOffsetInMilli = Util.TIME_ZONE_OFFSET_IN_MILLI
                                    )
                                // Return the budget with updated spent amount
                                budget.copy(spentAmount = amountYear)
                            }

                            PeriodType.ONE_TIME.id -> {
                                val amount =
                                    merchantDataRepository.getTotalAmountForBetweenDatesAndCategory(
                                        startTime = budget.startDate,
                                        endTime = budget.endDate ?: 0,
                                        categoryIds = budget.category,
                                        timeZoneOffsetInMilli = Util.TIME_ZONE_OFFSET_IN_MILLI
                                    )
                                // Return the budget with updated spent amount
                                budget.copy(spentAmount = amount)
                            }

                            else -> budget // Return the original budget if none of the conditions match
                        }
                    }
                    emit(bd)
                }
            }.flowOn(dispatcher) // Set the dispatcher for background tasks
    }
}
