package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.module.PaymentWithMode
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.PaymentRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetPaymentListWithModeUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun loadData(): Flow<List<PaymentWithMode>> {
        return paymentRepository.getPaymentListWithMode().flowOn(dispatcher)
    }

}