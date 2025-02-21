package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.module.balance.TotalWithCurrency
import com.indie.apps.pennypal.data.module.balance.toTotalWithCurrency
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.MerchantDataRepository
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.util.ShowDataPeriod
import com.indie.apps.pennypal.util.Util
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTotalFromPreferencePeriodUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    private val preferenceRepository: PreferenceRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun loadData(): Flow<TotalWithCurrency?> {

        val balanceViewValue = ShowDataPeriod.fromIndex(
            preferenceRepository.getInt(
                Util.PREF_BALANCE_VIEW,
                1
            )
        )

        val balanceFlow = when (balanceViewValue) {
            ShowDataPeriod.THIS_MONTH ->
                merchantDataRepository
                    .getTotalFromMonth(Util.TIME_ZONE_OFFSET_IN_MILLI, 0)
                    .map { it?.toTotalWithCurrency() }

            ShowDataPeriod.THIS_YEAR ->
                merchantDataRepository
                    .getTotalFromYear(Util.TIME_ZONE_OFFSET_IN_MILLI, 0)
                    .map { it?.toTotalWithCurrency() }

            ShowDataPeriod.ALL_TIME ->
                merchantDataRepository
                    .getTotal()
                    .map { it?.toTotalWithCurrency() }

            null ->
                merchantDataRepository
                    .getTotal()
                    .map { it?.toTotalWithCurrency() }
        }

        return balanceFlow
            .flowOn(dispatcher)
    }

}