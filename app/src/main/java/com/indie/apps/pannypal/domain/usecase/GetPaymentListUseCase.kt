package com.indie.apps.pannypal.domain.usecase

import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.repository.PaymentRepository
import com.indie.apps.pannypal.util.Constant
import com.indie.apps.pannypal.util.Resource
import com.indie.apps.pannypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetPaymentListUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend fun loadData(page : Int) : Flow<Resource<List<Payment>>>{
        return flow{

            try {
                emit(Resource.Loading())
                val merchantDataWithName = paymentRepository.getPaymentList(Constant.QUERY_PAGE_SIZE,Constant.QUERY_PAGE_SIZE * (page-1))
                emit(Resource.Success(merchantDataWithName))
            }catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

}