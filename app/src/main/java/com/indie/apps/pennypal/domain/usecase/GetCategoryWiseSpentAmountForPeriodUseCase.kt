package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.module.category.CategoryAmount
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.MerchantDataRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCategoryWiseSpentAmountForPeriodUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun loadCategoryWiseTotalAmountForMonth(
        year: Int,
        month: Int,
        categoryIds: List<Long>
    ): Flow<List<CategoryAmount>> {
        return merchantDataRepository.getCategoryWiseTotalAmountForMonth(
            year = year,
            monthPlusOne = month + 1,
            categoryIds = categoryIds,
            timeZoneOffsetInMilli = Util.TIME_ZONE_OFFSET_IN_MILLI
        ).flowOn(dispatcher)
    }

    fun loadCategoryWiseTotalAmountForYear(
        year: Int,
        categoryIds: List<Long>
    ):  Flow<List<CategoryAmount>> {
        return merchantDataRepository.getCategoryWiseTotalAmountForYear(
            year = year,
            categoryIds = categoryIds,
            timeZoneOffsetInMilli = Util.TIME_ZONE_OFFSET_IN_MILLI
        ).flowOn(dispatcher)
    }

    fun loadCategoryWiseTotalAmountForBetweenDates(
        startTime: Long,
        endTime: Long,
        categoryIds: List<Long>
    ):  Flow<List<CategoryAmount>> {
        return merchantDataRepository.getCategoryWiseTotalAmountForBetweenDates(
            startTime = startTime,
            endTime = endTime,
            categoryIds = categoryIds,
            timeZoneOffsetInMilli = Util.TIME_ZONE_OFFSET_IN_MILLI
        ).flowOn(dispatcher)
    }

}