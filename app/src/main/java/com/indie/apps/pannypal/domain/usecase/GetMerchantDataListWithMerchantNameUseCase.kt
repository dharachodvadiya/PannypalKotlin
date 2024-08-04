package com.indie.apps.pannypal.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.data.module.MerchantDataWithName
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.repository.MerchantDataRepository
import com.indie.apps.pannypal.util.Constant
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetMerchantDataListWithMerchantNameUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    /* suspend fun loadData(page : Int) : Flow<Resource<List<MerchantDataWithName>>>{
         return flow{

             try {
                 emit(Resource.Loading())
                 val merchantDataWithName = merchantDataRepository.getMerchantsDataWithMerchantNameList(Constant.QUERY_PAGE_SIZE,Constant.QUERY_PAGE_SIZE * (page-1))
                 emit(Resource.Success(merchantDataWithName))
             } catch (e: Throwable) {
                 emit(Resource.Error(handleException(e).message + ": ${e.message}"))
             }
         }.flowOn(dispatcher)
     }*/

    fun loadData(): Flow<PagingData<MerchantDataWithName>> {

        return Pager(
            PagingConfig(
                pageSize = Util.PAGE_SIZE,
                prefetchDistance = Util.PAGE_PREFETCH_DISTANCE
            )
        ) {
            merchantDataRepository.getMerchantsDataWithMerchantNameList(Constant.TIME_ZONE_OFFSET_IN_MILLI)
        }
            .flow
            .flowOn(dispatcher)
    }

}