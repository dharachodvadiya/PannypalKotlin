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

class deleteMultipleMerchantDataUsecase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    private val merchantRepository: MerchantRepository,
    private val userRepository: UserRepository,
    private val merchantsData: List<MerchantData>,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend operator fun invoke() : Flow<Resource<Int>>{
        return flow{

            try {
                emit(Resource.Loading<Int>())

                val ids = merchantsData.map{
                    it.id
                }
                val count = merchantDataRepository.deleteMerchantsWithId(ids)

                if(count == ids.size)
                {
                    val (newIncomeAmt, newExpenseAmt) = merchantsData.fold(0L to 0L) { acc, merchant ->
                        val (currentIncome, currentExpense) = acc
                        when {
                            merchant.amount < 0 -> (currentIncome to (currentExpense - (merchant.amount * -1)))
                            merchant.amount > 0 -> ((currentIncome - merchant.amount) to currentExpense)
                            else -> acc
                        }
                    }

                    val updatedRowMerchant = merchantRepository.updateAmountWithDate(
                        id = merchantsData[0].merchantId,
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