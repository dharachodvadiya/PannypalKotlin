package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.database.enum.PeriodType
import com.indie.apps.pennypal.repository.BudgetRepository
import com.indie.apps.pennypal.util.Util
import javax.inject.Inject

class GetBudgetsAndSpentWithCategoryIdListUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
) {
    fun loadData(
        year: Int,
        month: Int,
        dataPeriod: PeriodType
    ) = when (dataPeriod) {
        PeriodType.MONTH ->
            budgetRepository.getMonthBudgetsAndSpentWithCategoryIdListFromMonth(
                timeZoneOffsetInMilli = Util.TIME_ZONE_OFFSET_IN_MILLI,
                month = month,
                year = year
            )

        PeriodType.YEAR ->
            budgetRepository.getYearBudgetsAndSpentWithCategoryIdListFromYear(
                timeZoneOffsetInMilli = Util.TIME_ZONE_OFFSET_IN_MILLI,
                year = year
            )

        PeriodType.ONE_TIME ->
            budgetRepository.getOneTimeBudgetsWithCategoryIdListFromMonth(
                timeZoneOffsetInMilli = Util.TIME_ZONE_OFFSET_IN_MILLI,
                month = month,
                year = year
            )

    }
}