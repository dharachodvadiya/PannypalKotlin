package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.module.balance.Total
import com.indie.apps.pennypal.repository.MerchantDataRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.ShowDataPeriod
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.handleException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTotalUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
) {

    fun loadDataAsFlow(
        year: Int,
        month: Int,
        dataPeriod: ShowDataPeriod,
        toCurrencyId: Long,
    ) = when (dataPeriod) {
        ShowDataPeriod.THIS_MONTH ->
            merchantDataRepository
                .getTotalFromMonthAsFlow(
                    Util.TIME_ZONE_OFFSET_IN_MILLI,
                    year = year,
                    month = month,
                    toCurrencyId = toCurrencyId
                )

        ShowDataPeriod.THIS_YEAR ->
            merchantDataRepository
                .getTotalFromYearAsFlow(Util.TIME_ZONE_OFFSET_IN_MILLI, year, toCurrencyId)

        ShowDataPeriod.ALL_TIME ->
            merchantDataRepository
                .getTotalAsFlow(toCurrencyId)
    }

    fun loadData(
        year: Int,
        month: Int,
        dataPeriod: ShowDataPeriod,
        toCurrencyId: Long,
    ): Flow<Resource<Total>> {
        return flow {

            try {
                emit(Resource.Loading())

                val totalList = when (dataPeriod) {
                    ShowDataPeriod.THIS_MONTH ->
                        merchantDataRepository
                            .getTotalFromMonth(
                                Util.TIME_ZONE_OFFSET_IN_MILLI,
                                year = year,
                                month = month,
                                toCurrencyId = toCurrencyId
                            )

                    ShowDataPeriod.THIS_YEAR ->
                        merchantDataRepository
                            .getTotalFromYear(Util.TIME_ZONE_OFFSET_IN_MILLI, year, toCurrencyId)

                    ShowDataPeriod.ALL_TIME ->
                        merchantDataRepository
                            .getTotal(toCurrencyId)
                }

                emit(Resource.Success(totalList))
            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }
    }

}