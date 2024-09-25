package com.indie.apps.pennypal.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.indie.apps.pennypal.data.database.entity.Merchant
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.repository.MerchantRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchMerchantListUseCase @Inject constructor(
    private val merchantRepository: MerchantRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    /*suspend fun loadData(searchQuery : String) : Flow<PagingData<Merchant>>{
        return flow{

            try {
                emit(Resource.Loading())
                val merchantDataWithName = merchantRepository.searchMerchantList(searchQuery, Constant.QUERY_PAGE_SIZE,Constant.QUERY_PAGE_SIZE * (page-1))
                emit(Resource.Success(merchantDataWithName))
            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }*/

    fun loadData(searchQuery: String): Flow<PagingData<Merchant>> {

        return Pager(
            PagingConfig(
                pageSize = Util.PAGE_SIZE,
                prefetchDistance = Util.PAGE_PREFETCH_DISTANCE
            )
        ) {
            merchantRepository.searchMerchantList(searchQuery)
        }
            .flow
            .flowOn(dispatcher)
    }

}