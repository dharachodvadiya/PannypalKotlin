package com.indie.apps.pennypal.domain.usecase

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

class DeleteMultipleMerchantDataUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    private val merchantRepository: MerchantRepository,
    private val userRepository: UserRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    suspend fun deleteData(merchantId: Long, ids: List<Long>): Flow<Resource<Int>> {
        return flow {

            try {
                emit(Resource.Loading())
                val incomeAndExpense = merchantDataRepository.getTotalIncomeAndeExpenseFromIds(ids)
                val merchantDataDeleteCount =
                    merchantDataRepository.deleteMerchantDataWithIdList(ids)

                if (merchantDataDeleteCount == ids.size) {
                    /* val (newIncomeAmt, newExpenseAmt) = merchantsData.fold(0.0 to 0.0) { acc, merchant ->
                         val (currentIncome, currentExpense) = acc
                         when {
                             merchant.type < 0 -> (currentIncome to (currentExpense + merchant.amount))
                             merchant.type > 0 -> ((currentIncome + merchant.amount) to currentExpense)
                             else -> acc
                         }
                     }

                     handleReflectedTableOperation(count, newIncomeAmt, newExpenseAmt)*/

                    handleReflectedTableOperation(
                        merchantId,
                        merchantDataDeleteCount,
                        incomeAndExpense.totalIncome,
                        incomeAndExpense.totalExpense
                    )

                } else {
                    emit(Resource.Error("Fail to delete Multiple Merchant Data"))
                }


            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun FlowCollector<Resource<Int>>.handleReflectedTableOperation(
        merchantId: Long,
        affectedRowCount: Int,
        newIncome: Double,
        newExpense: Double
    ) {

        coroutineScope {
            val updatedRowMerchantDeferred = async {
                merchantRepository.updateAmountWithDate(
                    id = merchantId,
                    incomeAmt = -newIncome,
                    expenseAmt = -newExpense
                )
            }

            val updatedRowUserDeferred = async {
                userRepository.updateAmount(
                    incomeAmt = -newIncome,
                    expenseAmt = -newExpense
                )
            }
            updatedRowMerchantDeferred.await()
            updatedRowUserDeferred.await()
            val updatedRowMerchant = updatedRowMerchantDeferred.getCompleted()
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