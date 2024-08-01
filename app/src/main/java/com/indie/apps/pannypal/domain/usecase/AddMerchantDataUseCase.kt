package com.indie.apps.pannypal.domain.usecase

import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.repository.MerchantDataRepository
import com.indie.apps.pannypal.repository.MerchantRepository
import com.indie.apps.pannypal.repository.UserRepository
import com.indie.apps.pannypal.util.Resource
import com.indie.apps.pannypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddMerchantDataUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    private val merchantRepository: MerchantRepository,
    private val userRepository: UserRepository,
    private val merchantData: MerchantData,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(): Flow<Resource<Long>> {
        return flow {
            emit(Resource.Loading())

            try {
                val id = merchantDataRepository.insert(merchantData)

                if (id > 0) {
                    handleSuccessfulInsert(id)
                } else {
                    emit(Resource.Error("Failed to insert MerchantData"))
                }
            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

    private suspend fun FlowCollector<Resource<Long>>.handleSuccessfulInsert(id: Long) {
        merchantData.run {
            val updatedRowMerchant = merchantRepository.updateAmountWithDate(
                id = merchantId,
                incomeAmt = if(amount >= 0) amount else 0,
                expenseAmt = if(amount <0) (amount * -1) else 0,
                System.currentTimeMillis()
            )

            val updatedRowUser = userRepository.updateAmount(
                incomeAmt = if(amount >= 0) amount else 0,
                expenseAmt = if(amount <0) (amount * -1) else 0
            )

            emit(
                when {
                    updatedRowUser == 1 && updatedRowMerchant == 1 -> Resource.Success(id)
                    updatedRowUser == 1 && updatedRowMerchant == 0 -> Resource.Error("Fail to update Merchant Amount")
                    updatedRowUser == 0 && updatedRowMerchant == 1 -> Resource.Error("Fail to update User Amount")
                    else -> Resource.Error("Fail to update User And Merchant Amount")
                }
            )
        }
    }

}