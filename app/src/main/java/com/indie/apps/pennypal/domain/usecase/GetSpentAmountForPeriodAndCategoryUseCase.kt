package com.indie.apps.pennypal.domain.usecase

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

class GetSpentAmountForPeriodAndCategoryUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    suspend fun loadTotalAmountForMonth(
        year: Int,
        month: Int,
        categoryIds: List<Long>
    ): Flow<Resource<Double>> {
        return flow {

            try {
                emit(Resource.Loading())
                val total = merchantDataRepository.getTotalAmountForMonthAndCategory(
                    year = year,
                    monthPlusOne = month + 1,
                    categoryIds = categoryIds,
                    timeZoneOffsetInMilli = Util.TIME_ZONE_OFFSET_IN_MILLI
                )
                emit(Resource.Success(total))
            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

    suspend fun loadTotalAmountForYear(
        year: Int,
        categoryIds: List<Long>
    ): Flow<Resource<Double>> {
        return flow {

            try {
                emit(Resource.Loading())
                val total = merchantDataRepository.getTotalAmountForYearAndCategory(
                    year = year,
                    categoryIds = categoryIds,
                    timeZoneOffsetInMilli = Util.TIME_ZONE_OFFSET_IN_MILLI
                )
                emit(Resource.Success(total))
            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

    suspend fun loadTotalAmountForBetweenDates(
        startTime: Long,
        endTime: Long,
        categoryIds: List<Long>
    ): Flow<Resource<Double>> {
        return flow {

            try {
                emit(Resource.Loading())
                val total = merchantDataRepository.getTotalAmountForBetweenDatesAndCategory(
                    startTime = startTime,
                    endTime = endTime,
                    categoryIds = categoryIds,
                    timeZoneOffsetInMilli = Util.TIME_ZONE_OFFSET_IN_MILLI
                )
                emit(Resource.Success(total))
            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

}