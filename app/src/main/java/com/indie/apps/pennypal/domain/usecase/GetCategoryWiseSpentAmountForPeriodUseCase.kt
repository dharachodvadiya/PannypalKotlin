package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.module.category.CategoryAmount
import com.indie.apps.pennypal.repository.MerchantDataRepository
import com.indie.apps.pennypal.util.Util
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryWiseSpentAmountForPeriodUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository
) {

    fun loadCategoryWiseTotalAmountForMonth(
        year: Int,
        month: Int,
        categoryIds: List<Long>,
        toCurrencyId: Long,
    ): Flow<List<CategoryAmount>> {
        return merchantDataRepository.getCategoryWiseTotalAmountForMonth(
            year = year,
            month = month,
            categoryIds = categoryIds,
            timeZoneOffsetInMilli = Util.TIME_ZONE_OFFSET_IN_MILLI,
            toCurrencyId = toCurrencyId
        )
    }

    fun loadCategoryWiseTotalAmountForYear(
        year: Int,
        categoryIds: List<Long>,
        toCurrencyId: Long,
    ): Flow<List<CategoryAmount>> {
        return merchantDataRepository.getCategoryWiseTotalAmountForYear(
            year = year,
            categoryIds = categoryIds,
            timeZoneOffsetInMilli = Util.TIME_ZONE_OFFSET_IN_MILLI,
            toCurrencyId = toCurrencyId
        )
    }

    fun loadCategoryWiseTotalAmountForBetweenDates(
        startTime: Long,
        endTime: Long,
        categoryIds: List<Long>,
        toCurrencyId: Long,
    ): Flow<List<CategoryAmount>> {
        return merchantDataRepository.getCategoryWiseTotalAmountForBetweenDates(
            startTime = startTime,
            endTime = endTime,
            categoryIds = categoryIds,
            timeZoneOffsetInMilli = Util.TIME_ZONE_OFFSET_IN_MILLI,
            toCurrencyId = toCurrencyId
        )
    }

}