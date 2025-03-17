package com.indie.apps.pennypal.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.indie.apps.pennypal.data.module.merchant_data.MerchantDataWithAllData
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.MerchantDataRepository
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.util.ShowDataPeriod
import com.indie.apps.pennypal.util.Util
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchMerchantDataWithAllDataListUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    private val preferenceRepository: PreferenceRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    fun loadData(searchQuery: String): Flow<PagingData<MerchantDataWithAllData>> {

        return Pager(
            PagingConfig(
                pageSize = Util.PAGE_SIZE,
                prefetchDistance = Util.PAGE_PREFETCH_DISTANCE
            )
        ) {
            merchantDataRepository.searchMerchantsDataWithAllDataList(searchQuery)
        }
            .flow
            .flowOn(dispatcher)
    }

    fun getLast3DataFromPeriod(
        year: Int,
        month: Int
    ): Flow<List<MerchantDataWithAllData>> {

        val balanceViewValue = ShowDataPeriod.fromIndex(
            preferenceRepository.getInt(
                Util.PREF_BALANCE_VIEW,
                1
            )
        )

        return when (balanceViewValue) {
            ShowDataPeriod.THIS_MONTH ->
                merchantDataRepository.getRecentMerchantsDataWithAllDataListFromMonth(
                    Util.TIME_ZONE_OFFSET_IN_MILLI,
                    month
                )

            ShowDataPeriod.THIS_YEAR ->
                merchantDataRepository.getRecentMerchantsDataWithAllDataListFromYear(
                    Util.TIME_ZONE_OFFSET_IN_MILLI,
                    year
                )

            ShowDataPeriod.ALL_TIME ->
                merchantDataRepository.getRecentMerchantsDataWithAllDataList()

            null ->
                merchantDataRepository.getRecentMerchantsDataWithAllDataList()

        }
    }

}