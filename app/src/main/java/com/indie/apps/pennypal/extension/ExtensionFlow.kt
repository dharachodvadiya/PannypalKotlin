package com.indie.apps.pennypal.extension

import androidx.paging.PagingData
import androidx.paging.map
import com.indie.apps.pennypal.data.database.enum.PeriodType
import com.indie.apps.pennypal.data.module.Amount
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIdList
import com.indie.apps.pennypal.repository.MerchantDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar


fun <T> Flow<T>.mapBudgetsWithSpent(
    timeZoneOffsetInMilli: Int,
    merchantDataRepository: MerchantDataRepository,
): Flow<T> = flow {
    collect { value ->
        when (value) {
            is List<*> -> {
                @Suppress("UNCHECKED_CAST")
                val budgets = value as List<BudgetWithSpentAndCategoryIdList>
                val budgetWithSpentList = budgets.map { budget ->
                    val budgetWithSpent = budget
                    val spentAmount = calculateSpentAmount(
                        timeZoneOffsetInMilli,
                        merchantDataRepository,
                        budgetWithSpent
                    )
                    budgetWithSpent.copy(spentAmount = spentAmount.amount)
                }
                @Suppress("UNCHECKED_CAST")
                emit(budgetWithSpentList as T)
            }

            is PagingData<*> -> {
                @Suppress("UNCHECKED_CAST")
                val pagingData = value as PagingData<BudgetWithSpentAndCategoryIdList>
                val updatedPagingData = pagingData.map { budget ->
                    val budgetWithSpent = budget
                    val spentAmount = calculateSpentAmount(
                        timeZoneOffsetInMilli,
                        merchantDataRepository,
                        budgetWithSpent
                    )
                    budgetWithSpent.copy(spentAmount = spentAmount.amount)
                }
                @Suppress("UNCHECKED_CAST")
                emit(updatedPagingData as T)
            }

            else -> throw IllegalArgumentException("Unsupported flow type: ${value?.let { it::class } ?: "null"}")
        }
    }
}

// Calculate spent amount based on period type
private suspend fun calculateSpentAmount(
    timeZoneOffsetInMilli: Int,
    merchantDataRepository: MerchantDataRepository,
    budget: BudgetWithSpentAndCategoryIdList
): Amount {
    val startCal = Calendar.getInstance().apply { timeInMillis = budget.startDate }

    return when (budget.periodType) {
        PeriodType.MONTH.id -> {
            merchantDataRepository.getTotalAmountForMonthAndCategory(
                timeZoneOffsetInMilli = timeZoneOffsetInMilli,
                year = startCal.get(Calendar.YEAR),
                month = startCal.get(Calendar.MONTH),
                categoryIds = budget.category
            )
        }

        PeriodType.YEAR.id -> {
            merchantDataRepository.getTotalAmountForYearAndCategory(
                year = startCal.get(Calendar.YEAR),
                categoryIds = budget.category,
                timeZoneOffsetInMilli = timeZoneOffsetInMilli
            )
        }

        PeriodType.ONE_TIME.id -> {
            merchantDataRepository.getTotalAmountForBetweenDatesAndCategory(
                timeZoneOffsetInMilli = timeZoneOffsetInMilli,
                startTime = budget.startDate,
                endTime = budget.endDate ?: 0,
                categoryIds = budget.category
            )
        }

        else -> {
            Amount(0.0, "$", "US")
        }
    }
}