package com.indie.apps.pannypal.domain.usecase

import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails
import com.indie.apps.pannypal.repository.MerchantRepository
import com.indie.apps.pannypal.util.Constant
import com.indie.apps.pannypal.util.Resource
import com.indie.apps.pannypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchMerchantNameAndDetailListUseCase @Inject constructor(
    private val merchantRepository: MerchantRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend fun loadData(searchQuery : String, page : Int) : Flow<Resource<List<MerchantNameAndDetails>>>{
        return flow{

            try {
                emit(Resource.Loading())
                println("aaaaaa start Loading search page = $page")

                for(i in 1..5)
                {
                    println("aaaaaa Load search $i")
                    kotlinx.coroutines.delay(500L)
                }
                val merchantDataWithName = merchantRepository.searchMerchantNameAndDetailList(searchQuery, Constant.QUERY_PAGE_SIZE,Constant.QUERY_PAGE_SIZE * (page-1))
                emit(Resource.Success(merchantDataWithName))
            } catch (e: Throwable) {
                println("aaaaaa catch search ${e.message}")
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

}