package com.indie.apps.pannypal.domain.usecase

import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.repository.MerchantDataRepository
import com.indie.apps.pannypal.repository.MerchantRepository
import com.indie.apps.pannypal.repository.UserRepository
import com.indie.apps.pannypal.util.Resource
import com.indie.apps.pannypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UpdateMerchantDataUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    private val merchantRepository: MerchantRepository,
    private val userRepository: UserRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    suspend fun updateData(
        merchantDataNew: MerchantData,
        merchantDataOld: MerchantData
    ): Flow<Resource<Int>> {
        return flow {

            try {
                emit(Resource.Loading())
                val count = merchantDataRepository.update(merchantDataNew)

                if (count > 0) {

                    handleReflectedTableOperation(
                        affectedRowCount = count,
                        merchantDataOld = merchantDataOld,
                        merchantDataNew = merchantDataNew
                    )
                } else {
                    emit(Resource.Error<Int>("Fail to update merchantData"))
                }


            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

    private suspend fun FlowCollector<Resource<Int>>.handleReflectedTableOperation(
        affectedRowCount: Int,
        merchantDataNew: MerchantData,
        merchantDataOld: MerchantData
    ) {
        coroutineScope {

            var newIncomeAmt = 0.0
            var newExpenseAmt = 0.0

            merchantDataOld.run {
                when {
                    type < 0 -> newExpenseAmt -= (amount)
                    type > 0 -> newIncomeAmt -= (amount)
                }
            }
            merchantDataNew.run {
                when {
                    type < 0 -> newExpenseAmt += (amount)
                    type > 0 -> newIncomeAmt += (amount)
                }
            }
            var updatedRowMerchant = 0
            if (merchantDataNew.merchantId == merchantDataOld.merchantId) {
                val updatedRowMerchantDeferred = async {
                    merchantRepository.updateAmountWithDate(
                        id = merchantDataNew.merchantId,
                        incomeAmt = newIncomeAmt,
                        expenseAmt = newExpenseAmt,
                        dateInMilli = System.currentTimeMillis()
                    )
                }

                updatedRowMerchantDeferred.await()

                updatedRowMerchant = updatedRowMerchantDeferred.getCompleted()
            } else {
                val updatedRowMerchantDeferred1 = async {
                    merchantDataOld.run {
                        merchantRepository.updateAmountWithDate(
                            id = merchantId,
                            incomeAmt = if (type > 0) -amount else 0.0,
                            expenseAmt = if (type < 0) -amount else 0.0,
                            dateInMilli = System.currentTimeMillis()
                        )
                    }
                }

                val updatedRowMerchantDeferred2 = async {
                    merchantDataNew.run {
                        merchantRepository.updateAmountWithDate(
                            id = merchantId,
                            incomeAmt = if (type > 0) amount else 0.0,
                            expenseAmt = if (type < 0) amount else 0.0,
                            dateInMilli = System.currentTimeMillis()
                        )
                    }
                }

                updatedRowMerchantDeferred1.await()
                updatedRowMerchantDeferred2.await()

                if (updatedRowMerchantDeferred1.getCompleted() == 1 && updatedRowMerchantDeferred2.getCompleted() == 1)
                    updatedRowMerchant = updatedRowMerchantDeferred1.getCompleted()

            }


            val updatedRowUserDeferred = async {
                userRepository.updateAmount(
                    incomeAmt = newIncomeAmt,
                    expenseAmt = newExpenseAmt
                )
            }


            updatedRowUserDeferred.await()

            val updatedRowUser = updatedRowUserDeferred.getCompleted()

            emit(
                when {
                    updatedRowUser == 1 && updatedRowMerchant == 1 -> Resource.Success(
                        affectedRowCount
                    )

                    updatedRowUser == 1 && updatedRowMerchant == 0 -> Resource.Error("Fail to update Merchant Amount")
                    updatedRowUser == 0 && updatedRowMerchant == 1 -> Resource.Error("Fail to update User Amount")
                    else -> Resource.Error("Fail to update User And Merchant Amount")
                }
            )
        }

    }

}