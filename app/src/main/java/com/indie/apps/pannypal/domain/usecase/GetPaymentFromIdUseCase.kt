package com.indie.apps.pannypal.domain.usecase

import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.repository.MerchantDataRepository
import com.indie.apps.pannypal.repository.MerchantRepository
import com.indie.apps.pannypal.repository.PaymentRepository
import com.indie.apps.pannypal.util.Resource
import com.indie.apps.pannypal.util.handleException
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