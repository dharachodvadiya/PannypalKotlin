package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.database.db_entity.Payment
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.PaymentRepository
import com.indie.apps.pennypal.util.app_enum.Resource
import com.indie.apps.pennypal.util.app_enum.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddPaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun addPayment(payment: Payment): Flow<Resource<Long>> {
        return flow {
            emit(Resource.Loading())
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