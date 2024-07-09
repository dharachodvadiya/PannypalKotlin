package com.indie.apps.pannypal.domain.usecase

import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.repository.MerchantDataRepository
import com.indie.apps.pannypal.repository.MerchantRepository
import com.indie.apps.pannypal.repository.UserRepository
import com.indie.apps.pannypal.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class deleteSingleMerchantDataUsecase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    private val merchantRepository: MerchantRepository,
    private val userRepository: UserRepository,
    private val merchantData: MerchantData,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend operator fun invoke() : Flow<Resource<Int>>{
        return flow{

            try {
                emit(Resource.Loading<Int>())
                val count = merchantDataRepository.deleteMerchantDataWithId(merchantData.id)

                if(count >0)
                {
                    merchantData.run {
                        var newIncomeAmt = 0L
                        var newExpenseAmt = 0L

                        when{
                            amount < 0 -> newExpenseAmt -= (amount * -1)
                            amount > 0 -> newIncomeAmt -= (amount)
                        }
                        val updatedRowMerchant = merchantRepository.updateAmountWithDate(
                            id = merchantId,
                            incomeAmt = newIncomeAmt,
                            expenseAmt = newExpenseAmt,
                            dateInMilli = System.currentTimeMillis()
                        )

                        val updatedRowUser = userRepository.updateAmount(
                            incomeAmt = newIncomeAmt,
                            expenseAmt = newExpenseAmt
                        )

                        when {
                            updatedRowUser == 1 && updatedRowMerchant == 1 -> {
                                emit(Resource.Success<Int>(count))
                            }
                            updatedRowUser == 1 && updatedRowMerchant == 0 -> {
                                emit(Resource.Error<Int>("Merchant not updated"))
                            }
                            updatedRowUser == 0 && updatedRowMerchant == 1 -> {
                                emit(Resource.Error<Int>("User not updated"))
                            }
                            else -> {
                                emit(Resource.Error<Int>("User and Merchant not updated"))
                            }
                        }
                    }
                }else{
                    emit(Resource.Error<Int>("Merchant data not deleted"))
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