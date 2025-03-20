package com.indie.apps.pennypal.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIdList
import com.indie.apps.pennypal.data.module.budget.toBudgetWithSpentAndCategoryIdList
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.extension.mapBudgetsWithSpent
import com.indie.apps.pennypal.repository.BudgetRepository
import com.indie.apps.pennypal.repository.MerchantDataRepository
import com.indie.apps.pennypal.util.Util
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUpComingBudgetsAndSpentWithCategoryIdListFromPeriodType @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val merchantDataRepository: MerchantDataRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
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
            .mapBudgetsWithSpent(
                timeZoneOffsetInMilli = Util.TIME_ZONE_OFFSET_IN_MILLI,
                merchantDataRepository = merchantDataRepository,
            ).flowOn(dispatcher) // Set the dispatcher for background tasks
    }
}
