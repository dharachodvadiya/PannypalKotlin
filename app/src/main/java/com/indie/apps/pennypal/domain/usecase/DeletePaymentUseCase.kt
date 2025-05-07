package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.PaymentRepository
import com.indie.apps.pennypal.repository.UserRepository
import com.indie.apps.pennypal.util.app_enum.Resource
import com.indie.apps.pennypal.util.app_enum.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DeletePaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository,
    private val userRepository: UserRepository,
    //private val merchantDataRepository: MerchantDataRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun deleteData(deleteId: Long): Flow<Resource<Int>> {
        return flow {
            try {
                emit(Resource.Loading())
                val user = userRepository
                    .getUser().first()
                if (user.paymentId == deleteId) {
                    userRepository.updateWithDefaultPayment()
                }
                //merchantDataRepository.updateMerchantDataPaymentId(deleteId, replaceId)
                val id = paymentRepository.deleteCustomPayment(deleteId)
                if (id > 0) {
                    emit(Resource.Success(id))
                } else {
                    emit(Resource.Error("Fail to delete payment"))
                }


            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

}