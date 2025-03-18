package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.module.balance.Total
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.ExchangeRateRepository
import com.indie.apps.pennypal.repository.MerchantDataRepository
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.repository.UserRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.ShowDataPeriod
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTotalUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val userRepository: UserRepository,
    private val preferenceRepository: PreferenceRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun loadDataAsFlow(
        year: Int,
        month: Int,
        dataPeriod: ShowDataPeriod
    ): Flow<Total> {


        return flow {

            /* val balanceViewValue = ShowDataPeriod.fromIndex(
            preferenceRepository.getInt(
                Util.PREF_BALANCE_VIEW,
                1
            )
        )
*/
            val baseCurrCode = userRepository.getCurrencyCountryCode().first()
            val baseCurrencySymbol = userRepository.getCurrency().first()

            val balanceFlow = when (dataPeriod) {
                ShowDataPeriod.THIS_MONTH ->
                    merchantDataRepository
                        .getTotalFromMonthAsFlow(
                            Util.TIME_ZONE_OFFSET_IN_MILLI,
                            year = year,
                            month = month
                        )

                ShowDataPeriod.THIS_YEAR ->
                    merchantDataRepository
                        .getTotalFromYearAsFlow(Util.TIME_ZONE_OFFSET_IN_MILLI, year)

                ShowDataPeriod.ALL_TIME ->
                    merchantDataRepository
                        .getTotalAsFlow()
            }.map { list ->

                convertAmount(
                    list = list,
                    baseCurrencySymbol = baseCurrencySymbol,
                    baseCurrCode = baseCurrCode
                )
            }

            emitAll(balanceFlow)
        }.flowOn(dispatcher)
    }

    fun loadData(
        year: Int,
        month: Int,
        dataPeriod: ShowDataPeriod
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
                                month = month
                            )

                    ShowDataPeriod.THIS_YEAR ->
                        merchantDataRepository
                            .getTotalFromYear(Util.TIME_ZONE_OFFSET_IN_MILLI, year)

                    ShowDataPeriod.ALL_TIME ->
                        merchantDataRepository
                            .getTotal()
                }

                val baseCurrCode = userRepository.getCurrencyCountryCode().first()
                val baseCurrencySymbol = userRepository.getCurrency().first()

                val finalTotal = convertAmount(
                    list = totalList,
                    baseCurrCode = baseCurrCode,
                    baseCurrencySymbol = baseCurrencySymbol
                )
                emit(Resource.Success(finalTotal))
            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

    private suspend fun convertAmount(
        list: List<Total>,
        baseCurrCode: String,
        baseCurrencySymbol: String
    ): Total = coroutineScope {
        var totalExpense = 0.0
        var totalIncome = 0.0

        val deferredRates = list.map { total ->
            async {
                val rateResult = exchangeRateRepository.getConversionRate(
                    fromCurrencyCountry = total.baseCurrencyCountryCode,
                    toCurrencyCountry = baseCurrCode
                )
                when (rateResult) {
                    is Resource.Success -> rateResult.data!!
                    else -> 1.0 // Fallback for failure
                }
            }
        }

        val rates = deferredRates.awaitAll()

        list.forEachIndexed { index, total ->
            val rate = rates[index]
            totalExpense += exchangeRateRepository.getAmountFromRate(
                amount = total.totalExpense,
                rate = rate
            )
            totalIncome += exchangeRateRepository.getAmountFromRate(
                amount = total.totalIncome,
                rate = rate
            )
        }

        Total(
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            baseCurrencySymbol = baseCurrencySymbol,
            baseCurrencyCountryCode = baseCurrCode
        )
    }

}