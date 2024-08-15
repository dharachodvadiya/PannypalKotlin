package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.entity.MerchantData
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.MerchantDataRepository
import com.indie.apps.pennypal.repository.MerchantRepository
import com.indie.apps.pennypal.repository.UserRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddMerchantDataUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    private val merchantRepository: MerchantRepository,
    private val userRepository: UserRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    suspend fun addData(merchantData: MerchantData): Flow<Resource<Long>> {
        return flow {
            emit(Resource.Loading())

            try {
                val id = merchantDataRepository.insert(merchantData)

                if (id > 0) {
                    handleSuccessfulInsert(
                        id = id,
                        merchantId = merchantData.merchantId,
                        amount = merchantData.amount,
                        type = merchantData.type
                    )
                } else {
                    emit(Resource.Error("Failed to insert MerchantData"))
                }
            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun FlowCollector<Resource<Long>>.handleSuccessfulInsert(
        id: Long,
        merchantId: Long,
        amount: Double,
        type: Int
    ) {

        coroutineScope {
            val updatedRowMerchantDeferred = async {
                merchantRepository.updateAmountWithDate(
                    id = merchantId,
                    incomeAmt = if (type > 0) amount else 0.0,
                    expenseAmt = if (type < 0) amount else 0.0,
                    System.currentTimeMillis()
                )
            }

            val updatedRowUserDeferred = async {
                userRepository.updateAmount(
                    incomeAmt = if (type > 0) amount else 0.0,
                    expenseAmt = if (type < 0) (amount) else 0.0
                )
            }
            updatedRowMerchantDeferred.await()
            updatedRowUserDeferred.await()
            val updatedRowMerchant = updatedRowMerchantDeferred.getCompleted()
            val updatedRowUser = updatedRowUserDeferred.getCompleted()

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