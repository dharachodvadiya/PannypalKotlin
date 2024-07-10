package com.indie.apps.pannypal.domain.usecase

import android.database.sqlite.SQLiteConstraintException
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

                val ids = merchantsData.map{ it.id }
                val count = merchantDataRepository.deleteMerchantDataWithIdList(ids)

                if(count == ids.size){
                    val (newIncomeAmt, newExpenseAmt) = merchantsData.fold(0L to 0L) { acc, merchant ->
                        val (currentIncome, currentExpense) = acc
                        when {
                            merchant.amount < 0 -> (currentIncome to (currentExpense - (merchant.amount * -1)))
                            merchant.amount > 0 -> ((currentIncome - merchant.amount) to currentExpense)
                            else -> acc
                        }
                    }

                    handleReflectedTableOperation(count, newIncomeAmt, newExpenseAmt)

                }else{
                    emit(Resource.Error<Int>("Fail to delete Multiple Merchant Data"))
                }


            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

    private suspend fun FlowCollector<Resource<Int>>.handleReflectedTableOperation(affectedRowCount: Int, newIncome : Long, newExpense: Long ) {
        val updatedRowMerchant = merchantRepository.updateAmountWithDate(
            id = merchantsData[0].merchantId,
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