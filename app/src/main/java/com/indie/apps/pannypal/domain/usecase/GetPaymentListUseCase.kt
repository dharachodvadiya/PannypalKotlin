package com.indie.apps.pannypal.domain.usecase

import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.repository.PaymentRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetPaymentListUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun loadData(): Flow<List<Payment>> {
        /*return flow{

            try {
                val merchantDataWithName = paymentRepository.getPaymentList()
                emit(Resource.Success(merchantDataWithName))
            }catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)*/

        return paymentRepository.getPaymentList().flowOn(dispatcher)
    }

}