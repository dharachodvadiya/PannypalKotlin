package com.indie.apps.pennypal.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.indie.apps.pennypal.data.module.MerchantDataWithPaymentName
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.MerchantDataRepository
import com.indie.apps.pennypal.util.Util
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetMerchantDataWithPaymentNameListFromMerchantIdUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    /* suspend fun loadData(merchantId: Long) : Flow<Resource<List<MerchantData>>>{
         return flow{

             try {
                 emit(Resource.Loading())
                 val merchantsDataFromId = merchantDataRepository.getMerchantDataListFromMerchantId(merchantId, Constant.QUERY_PAGE_SIZE,Constant.QUERY_PAGE_SIZE * (page-1))
                 emit(Resource.Success(merchantsDataFromId))
             } catch (e: Throwable) {
                 emit(Resource.Error(handleException(e).message + ": ${e.message}"))
             }
         }.flowOn(dispatcher)
     }*/

    fun loadData(merchantId: Long): Flow<PagingData<MerchantDataWithPaymentName>> {

        return Pager(
            PagingConfig(
                pageSize = Util.PAGE_SIZE,
                prefetchDistance = Util.PAGE_PREFETCH_DISTANCE
            )
        ) {
            merchantDataRepository.getMerchantsDataWithPaymentNameListFromMerchantId(merchantId)
        }
            .flow
            .flowOn(dispatcher)
    }

}