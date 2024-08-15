package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.entity.Payment
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.PaymentRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetPaymentFromIdUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    fun getData(id: Long) : Flow<Resource<Payment>>{
        return flow{
            if(id != 0L)
            {
                try {
                    emit(Resource.Loading())
                    val merchantFromId = paymentRepository.getPaymentFromId(id)
                    emit(Resource.Success(merchantFromId))
                }catch (e: Throwable) {
                    emit(Resource.Error(handleException(e).message + ": ${e.message}"))
                }
            }else{
                emit(Resource.Error("Data Not Found"))
            }


        }.flowOn(dispatcher)
    }

}