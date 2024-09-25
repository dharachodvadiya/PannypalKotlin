package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.database.entity.Payment
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.PaymentRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetPaymentFromIdUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun getData(id: Long): Flow<Payment> {
        return flow {
            emit(paymentRepository.getPaymentFromId(id))
        }.flowOn(dispatcher)
    }

}