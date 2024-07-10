package com.indie.apps.pannypal.domain.usecase

import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.data.module.MerchantDataWithName
import com.indie.apps.pannypal.repository.MerchantDataRepository
import com.indie.apps.pannypal.util.Constant
import com.indie.apps.pannypal.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class searchMerchantDataWithMerchantNameListUsecase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend fun loadData(searchQuery : String, page : Int) : Flow<Resource<List<MerchantDataWithName>>>{
        return flow{

            try {
                emit(Resource.Loading<List<MerchantDataWithName>>())
                val merchantDataWithName = merchantDataRepository.searchMerchantDataWithMerchantNameList(searchQuery,Constant.QUERY_PAGE_SIZE,Constant.QUERY_PAGE_SIZE * (page-1))
                emit(Resource.Success<List<MerchantDataWithName>>(merchantDataWithName))
            } catch(e: Throwable) {
                when(e) {
                    is IOException -> emit(Resource.Error<List<MerchantDataWithName>>("Network Failure"))
                    else -> emit(Resource.Error<List<MerchantDataWithName>>("Conversion Error"))
                }
            }
        }.flowOn(dispatcher)
    }

}