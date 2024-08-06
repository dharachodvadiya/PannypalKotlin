package com.indie.apps.pannypal.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.repository.MerchantRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchMerchantNameAndDetailListUseCase @Inject constructor(
    private val merchantRepository: MerchantRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    fun loadData(searchQuery : String) : Flow<PagingData<MerchantNameAndDetails>>{

        return Pager(
            PagingConfig(pageSize = Util.PAGE_SIZE,
                prefetchDistance = Util.PAGE_PREFETCH_DISTANCE)
        ) {
            merchantRepository.searchMerchantNameAndDetailList(searchQuery)
        }
            .flow
            .flowOn(dispatcher)
    }

  /*  fun loadData(searchQuery : String) : Flow<PagingData<MerchantNameAndDetails>>{

        return merchantRepository
            .searchMerchantNameAndDetailListPaging(searchQuery)
            .flowOn(dispatcher)
    }*/

}