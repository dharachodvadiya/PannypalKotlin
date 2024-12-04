package com.indie.apps.pennypal.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.indie.apps.pennypal.data.module.MerchantDataWithAllData
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.MerchantDataRepository
import com.indie.apps.pennypal.util.Util
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchMerchantDataWithAllDataListUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    fun loadData(searchQuery : String): Flow<PagingData<MerchantDataWithAllData>> {

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

    fun getLast3Data(): Flow<List<MerchantDataWithAllData>> {

        return merchantDataRepository.getRecentMerchantsDataWithAllDataList()
            .flowOn(dispatcher)
    }

}