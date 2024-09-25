package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.database.entity.PaymentMode
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.PaymentModeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetPaymentModeListUseCase @Inject constructor(
    private val paymentModeRepository: PaymentModeRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun loadData(): Flow<List<PaymentMode>> {
        return paymentModeRepository.getPaymentModeList().flowOn(dispatcher)
    }

}