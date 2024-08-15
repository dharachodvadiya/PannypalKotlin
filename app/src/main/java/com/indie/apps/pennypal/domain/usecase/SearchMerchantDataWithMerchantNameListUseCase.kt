package com.indie.apps.pennypal.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.data.module.MerchantDataWithName
import com.indie.apps.pennypal.presentation.ui.common.Util
import com.indie.apps.pennypal.repository.MerchantDataRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchMerchantDataWithMerchantNameListUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    /*suspend fun loadData(searchQuery : String) : Flow<Resource<List<MerchantDataWithName>>>{
        return flow{

            try {
                emit(Resource.Loading())
                val merchantDataWithName = merchantDataRepository.searchMerchantDataWithMerchantNameList(searchQuery = searchQuery)
                emit(Resource.Success(merchantDataWithName))
            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }*/

    fun loadData(searchQuery : String) : Flow<PagingData<MerchantDataWithName>>{

        return Pager(
            PagingConfig(pageSize = Util.PAGE_SIZE,
                prefetchDistance = Util.PAGE_PREFETCH_DISTANCE)
        ) {
            merchantDataRepository.searchMerchantDataWithMerchantNameList(searchQuery,Util.TIME_ZONE_OFFSET_IN_MILLI)
        }
            .flow
            .flowOn(dispatcher)
    }


}