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

class GetMerchantListUseCase @Inject constructor(
    private val merchantRepository: MerchantRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

   /* suspend fun loadData(page : Int) : Flow<Resource<List<Merchant>>>{
        return flow{

            try {
                emit(Resource.Loading())
                val merchantDataWithName = merchantRepository.getMerchantList(Util.QUERY_PAGE_SIZE,Util.QUERY_PAGE_SIZE * (page-1))
                emit(Resource.Success(merchantDataWithName))
            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }*/

    fun loadData() : Flow<PagingData<Merchant>>{

        return Pager(
            PagingConfig(pageSize = Util.PAGE_SIZE,
                prefetchDistance = Util.PAGE_PREFETCH_DISTANCE)
        ) {
            merchantRepository.getMerchantList()
        }
            .flow
            .flowOn(dispatcher)
    }

}