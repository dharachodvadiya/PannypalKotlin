package com.indie.apps.pannypal.domain.usecase

import android.database.sqlite.SQLiteConstraintException
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.repository.MerchantDataRepository
import com.indie.apps.pannypal.repository.MerchantRepository
import com.indie.apps.pannypal.repository.UserRepository
import com.indie.apps.pannypal.util.Resource
import com.indie.apps.pannypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class deleteMultipleMerchantUsecase @Inject constructor(
    private val merchantRepository: MerchantRepository,
    private val merchantDataRepository: MerchantDataRepository,
    private val userRepository: UserRepository,
    private val merchants : List<Merchant>,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend operator fun invoke() : Flow<Resource<Int>>{
        return flow{

            try {
                emit(Resource.Loading())
                val ids = merchants.map{
                    it.id
                }
                val merchantDeleteCount = merchantRepository.deleteMerchantWithIdList(ids)

                if(merchantDeleteCount == ids.size){

                    val (incomeAmt, expenseAmt) = merchants
                        .fold(0L to 0L) { acc, merchant ->
                            val (totalIncome, totalExpense) = acc
                            totalIncome + merchant.incomeAmount to totalExpense + merchant.expenseAmount
                        }

                    merchantDataRepository.deleteMerchantDataWithMerchantIdList(ids)
                    val updatedRowUser = userRepository.updateAmount(-incomeAmt, -expenseAmt)

                    emit(
                        when {
                            updatedRowUser == 1 -> Resource.Success(merchantDeleteCount)
                            else -> Resource.Error("Fail to update User Amount")
                        }
                    )

                }else{
                    emit(Resource.Error("Fail to delete multiple merchant"))
                }


            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

}