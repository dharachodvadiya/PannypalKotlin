package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.database.entity.Payment
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.PaymentRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UpdatePaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend fun updateData(payment: Payment) : Flow<Resource<Int>>{
        return flow{

            try {
                emit(Resource.Loading())
                val count = paymentRepository.update(payment)

                if(count >0){
                    emit(Resource.Success(count))
                }else{
                    emit(Resource.Error("Fail to update Payment"))
                }


            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

}