package com.indie.apps.pannypal.domain.usecase

import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.repository.PaymentRepository
import com.indie.apps.pannypal.util.Resource
import com.indie.apps.pannypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class addPaymentUsecase @Inject constructor(
    private val paymentRepository: PaymentRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    suspend fun addPayment(payment: Payment): Flow<Resource<Long>> {
        return flow {

            try {
                val id = paymentRepository.insert(payment)
                if (id > 0) {
                    emit(Resource.Success(id))
                } else {
                    emit(Resource.Error("Fail to add payment"))
                }

            } catch (e: Throwable) {

                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

}