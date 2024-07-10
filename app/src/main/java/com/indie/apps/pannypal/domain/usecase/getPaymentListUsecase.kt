package com.indie.apps.pannypal.domain.usecase

import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.repository.PaymentRepository
import com.indie.apps.pannypal.util.Constant
import com.indie.apps.pannypal.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class getPaymentListUsecase @Inject constructor(
    private val paymentRepository: PaymentRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend fun loadData(page : Int) : Flow<Resource<List<Payment>>>{
        return flow{

            try {
                emit(Resource.Loading<List<Payment>>())
                val merchantDataWithName = paymentRepository.getPaymentList(Constant.QUERY_PAGE_SIZE,Constant.QUERY_PAGE_SIZE * (page-1))
                emit(Resource.Success<List<Payment>>(merchantDataWithName))
            } catch(e: Throwable) {
                when(e) {
                    is IOException -> emit(Resource.Error<List<Payment>>("Network Failure"))
                    else -> emit(Resource.Error<List<Payment>>("Conversion Error"))
                }
            }
        }.flowOn(dispatcher)
    }

}