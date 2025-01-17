package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.database.entity.MerchantData
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.MerchantDataRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DeleteSingleMerchantDataUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    private val merchantData: MerchantData,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    operator fun invoke(): Flow<Resource<Int>> {
        return flow {

            try {
                emit(Resource.Loading())
                val count = merchantDataRepository.deleteMerchantDataWithId(merchantData.id)

                if (count > 0) {
                    /*merchantData.run {
                        var newIncomeAmt = 0.0
                        var newExpenseAmt = 0.0

                        when {
                            type < 0 -> newExpenseAmt += amount
                            type > 0 -> newIncomeAmt += (amount)
                        }

                        handleReflectedTableOperation(count, newIncomeAmt, newExpenseAmt)

                    }*/
                    emit(Resource.Success(count))
                } else {
                    emit(Resource.Error("Fail to delete Single Merchant Data"))
                }

            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

    /*private suspend fun FlowCollector<Resource<Int>>.handleReflectedTableOperation(
        affectedRowCount: Int,
        newIncome: Double,
        newExpense: Double
    ) {
        merchantData.run {
            val updatedRowMerchant = merchantRepository.updateAmountWithDate(
                id = merchantId,
                incomeAmt = -newIncome,
                expenseAmt = -newExpense
            )

            val updatedRowUser = userRepository.updateAmount(
                incomeAmt = -newIncome,
                expenseAmt = -newExpense
            )

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

    }*/

}