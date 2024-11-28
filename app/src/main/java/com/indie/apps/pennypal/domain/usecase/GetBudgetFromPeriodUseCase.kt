package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIdList
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.BudgetRepository
import com.indie.apps.pennypal.util.Util
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetBudgetFromPeriodUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    fun loadFromMonth(year: Int, month: Int): Flow<List<BudgetWithSpentAndCategoryIdList>> {
        return budgetRepository.getBudgetsWithCategoryIdListFromMonth(
            month = month,
            year = year,
            timeZoneOffsetInMilli = Util.TIME_ZONE_OFFSET_IN_MILLI
        )
            .flowOn(dispatcher)

    }

}