package com.indie.apps.pannypal.domain.usecase

import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails
import com.indie.apps.pannypal.repository.MerchantRepository
import com.indie.apps.pannypal.util.Constant
import com.indie.apps.pannypal.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class getMerchantNameAndDetailListUsecase @Inject constructor(
    private val merchantRepository: MerchantRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend fun loadData(page : Int) : Flow<Resource<List<MerchantNameAndDetails>>>{
        return flow{

            try {
                emit(Resource.Loading<List<MerchantNameAndDetails>>())
                val merchantDataWithName = merchantRepository.getMerchantNameAndDetailList(Constant.QUERY_PAGE_SIZE,Constant.QUERY_PAGE_SIZE * (page-1))
                emit(Resource.Success<List<MerchantNameAndDetails>>(merchantDataWithName))
            } catch(e: Throwable) {
                when(e) {
                    is IOException -> emit(Resource.Error<List<MerchantNameAndDetails>>("Network Failure"))
                    else -> emit(Resource.Error<List<MerchantNameAndDetails>>("Conversion Error"))
                }
            }
        }.flowOn(dispatcher)
    }

}