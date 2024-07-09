package com.indie.apps.pannypal.domain.usecase

import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.repository.PaymentRepository
import com.indie.apps.pannypal.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class updatePaymentUsecase @Inject constructor(
    private val paymentRepository: PaymentRepository,
    private val payment: Payment,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend operator fun invoke() : Flow<Resource<Int>>{
        return flow{

            try {
                emit(Resource.Loading<Int>())
                val count = paymentRepository.update(payment)

                if(count >0)
                {
                    emit(Resource.Success<Int>(count))
                }else{
                    emit(Resource.Error<Int>("Payment not updated"))
                }


            } catch(e: Throwable) {
                when(e) {
                    is IOException -> emit(Resource.Error<Int>("Network Failure"))
                    else -> emit(Resource.Error<Int>("Conversion Error"))
                }
            }
        }.flowOn(dispatcher)
    }

}