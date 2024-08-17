package com.indie.apps.pennypal.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.indie.apps.pennypal.data.entity.Payment
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.repository.PaymentRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchPaymentListUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    /*suspend fun loadData(searchQuery : String, page : Int) : Flow<Resource<List<Payment>>>{
        return flow{

            try {
                emit(Resource.Loading())
                val merchantDataWithName = paymentRepository.searchPaymentList(searchQuery, Util.QUERY_PAGE_SIZE,Util.QUERY_PAGE_SIZE * (page-1))
                emit(Resource.Success(merchantDataWithName))
            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }*/

    fun loadData(searchQuery : String) : Flow<PagingData<Payment>>{

        return Pager(
            PagingConfig(pageSize = Util.PAGE_SIZE,
                prefetchDistance = Util.PAGE_PREFETCH_DISTANCE)
        ) {
            paymentRepository.searchPaymentList(searchQuery)
        }
            .flow
            .flowOn(dispatcher)
    }

}