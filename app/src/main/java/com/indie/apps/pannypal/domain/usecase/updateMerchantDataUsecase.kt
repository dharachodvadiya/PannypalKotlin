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
import java.io.IOException
import javax.inject.Inject

class updateMerchantDataUsecase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    private val merchantRepository: MerchantRepository,
    private val userRepository: UserRepository,
    private val merchantDataNew: MerchantData,
    private val merchantDataOld: MerchantData,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend operator fun invoke() : Flow<Resource<Int>>{
        return flow{

            try {
                emit(Resource.Loading<Int>())
                val count = merchantDataRepository.update(merchantDataOld)

                if(count >0){
                    merchantDataNew.run {
                        var newIncomeAmt = 0L
                        var newExpenseAmt = 0L

                        merchantDataOld.run {
                            when{
                                amount < 0 -> newExpenseAmt -= (amount * -1)
                                amount > 0 -> newIncomeAmt -= (amount)
                            }
                        }

                        when{
                            amount < 0 -> newExpenseAmt += (amount * -1)
                            amount > 0 -> newIncomeAmt += (amount)
                        }

                        handleReflectedTableOperation(count, newIncomeAmt, newExpenseAmt)

                    }
                }else{
                    emit(Resource.Error<Int>("Fail to update merchantData"))
                }


            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

    private suspend fun FlowCollector<Resource<Int>>.handleReflectedTableOperation(affectedRowCount: Int, newIncome : Long, newExpense: Long ) {
        val updatedRowMerchant = merchantRepository.updateAmountWithDate(
            id = merchantDataNew.merchantId,
            incomeAmt = newIncome,
            expenseAmt = newExpense,
            dateInMilli = System.currentTimeMillis()
        )

        val updatedRowUser = userRepository.updateAmount(
            incomeAmt = newIncome,
            expenseAmt = newExpense
        )

        emit(
            when {
                updatedRowUser == 1 && updatedRowMerchant == 1 -> Resource.Success(affectedRowCount)
                updatedRowUser == 1 && updatedRowMerchant == 0 -> Resource.Error("Fail to update Merchant Amount")
                updatedRowUser == 0 && updatedRowMerchant == 1 -> Resource.Error("Fail to update User Amount")
                else -> Resource.Error("Fail to update User And Merchant Amount")
            }
        )
    }

}