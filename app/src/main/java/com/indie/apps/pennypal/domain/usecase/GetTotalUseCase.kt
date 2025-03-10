package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.module.balance.Total
import com.indie.apps.pennypal.data.module.balance.toTotalWithCurrency
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.MerchantDataRepository
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.ShowDataPeriod
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTotalUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    private val preferenceRepository: PreferenceRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun loadDataAsFlow(
        year: Int,
        month: Int,
        dataPeriod: ShowDataPeriod
    ): Flow<Total?> {

        /* val balanceViewValue = ShowDataPeriod.fromIndex(
             preferenceRepository.getInt(
                 Util.PREF_BALANCE_VIEW,
                 1
             )
         )
 */
        val balanceFlow = when (dataPeriod) {
            ShowDataPeriod.THIS_MONTH ->
                merchantDataRepository
                    .getTotalFromMonthAsFlow(
                        Util.TIME_ZONE_OFFSET_IN_MILLI,
                        year = year,
                        month = month
                    )
                    .map { it?.toTotalWithCurrency() }

            ShowDataPeriod.THIS_YEAR ->
                merchantDataRepository
                    .getTotalFromYearAsFlow(Util.TIME_ZONE_OFFSET_IN_MILLI, year)
                    .map { it?.toTotalWithCurrency() }

            ShowDataPeriod.ALL_TIME ->
                merchantDataRepository
                    .getTotalAsFlow()
                    .map { it?.toTotalWithCurrency() }
        }

        return balanceFlow
            .flowOn(dispatcher)
    }

    fun loadData(
        year: Int,
        month: Int,
        dataPeriod: ShowDataPeriod
    ): Flow<Resource<Total?>> {
        return flow {

            try {
                emit(Resource.Loading())

                val total = when (dataPeriod) {
                    ShowDataPeriod.THIS_MONTH ->
                        merchantDataRepository
                            .getTotalFromMonth(
                                Util.TIME_ZONE_OFFSET_IN_MILLI,
                                year = year,
                                month = month
                            )
                            ?.toTotalWithCurrency()

                    ShowDataPeriod.THIS_YEAR ->
                        merchantDataRepository
                            .getTotalFromYear(Util.TIME_ZONE_OFFSET_IN_MILLI, year)
                            ?.toTotalWithCurrency()

                    ShowDataPeriod.ALL_TIME ->
                        merchantDataRepository
                            .getTotal()
                            ?.toTotalWithCurrency()
                }

                emit(Resource.Success(total))
            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

}