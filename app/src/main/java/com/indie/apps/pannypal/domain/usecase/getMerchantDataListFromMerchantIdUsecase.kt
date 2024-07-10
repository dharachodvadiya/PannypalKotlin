package com.indie.apps.pannypal.domain.usecase

import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.repository.MerchantDataRepository
import com.indie.apps.pannypal.util.Constant
import com.indie.apps.pannypal.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class getMerchantDataListFromMerchantIdUsecase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend fun loadData(merchantId: Long,page : Int) : Flow<Resource<List<MerchantData>>>{
        return flow{

            try {
                emit(Resource.Loading<List<MerchantData>>())
                val merchantsDataFromId = merchantDataRepository.getMerchantDataListFromMerchantId(merchantId, Constant.QUERY_PAGE_SIZE,Constant.QUERY_PAGE_SIZE * (page-1))
                emit(Resource.Success<List<MerchantData>>(merchantsDataFromId))
            } catch(e: Throwable) {
                when(e) {
                    is IOException -> emit(Resource.Error<List<MerchantData>>("Network Failure"))
                    else -> emit(Resource.Error<List<MerchantData>>("Conversion Error"))
                }
            }
        }.flowOn(dispatcher)
    }

}