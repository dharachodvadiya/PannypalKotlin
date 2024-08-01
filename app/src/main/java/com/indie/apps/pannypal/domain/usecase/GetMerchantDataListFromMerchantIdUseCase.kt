package com.indie.apps.pannypal.domain.usecase

import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.repository.MerchantDataRepository
import com.indie.apps.pannypal.util.Constant
import com.indie.apps.pannypal.util.Resource
import com.indie.apps.pannypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetMerchantDataListFromMerchantIdUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend fun loadData(merchantId: Long,page : Int) : Flow<Resource<List<MerchantData>>>{
        return flow{

            try {
                emit(Resource.Loading())
                val merchantsDataFromId = merchantDataRepository.getMerchantDataListFromMerchantId(merchantId, Constant.QUERY_PAGE_SIZE,Constant.QUERY_PAGE_SIZE * (page-1))
                emit(Resource.Success(merchantsDataFromId))
            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

}