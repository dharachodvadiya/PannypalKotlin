package com.indie.apps.pannypal.domain.usecase

import com.indie.apps.pannypal.data.entity.Merchant
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

class deleteMultipleMerchantUsecase @Inject constructor(
    private val merchantRepository: MerchantRepository,
    private val merchantDataRepository: MerchantDataRepository,
    private val userRepository: UserRepository,
    private val merchants : List<Merchant>,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend operator fun invoke() : Flow<Resource<Int>>{
        return flow{

            try {
                emit(Resource.Loading<Int>())
                val ids = merchants.map{
                    it.id
                }
                val merchantDeleteCount = merchantRepository.deleteMerchantWithIdList(ids)

                if(merchantDeleteCount == ids.size)
                {
                    val (incomeAmt, expenseAmt) = merchants
                        .fold(0L to 0L) { acc, merchant ->
                            val (totalIncome, totalExpense) = acc
                            totalIncome + merchant.incomeAmount to totalExpense + merchant.expenseAmount
                        }

                    merchantDataRepository.deleteMerchantDataWithMerchantIdList(ids)
                    val userUpdateCount = userRepository.updateAmount(-incomeAmt, -expenseAmt)

                    when {
                        userUpdateCount > 0 -> {
                            emit(Resource.Success<Int>(merchantDeleteCount))
                        }
                        else -> {
                            emit(Resource.Error<Int>("User and Merchant Data not updated"))
                        }
                    }

                }else{
                    emit(Resource.Error<Int>("Merchant not delete"))
                }


            } catch(e: Throwable) {
                when(e) {
                    is IOException -> emit(Resource.Error<Int>("Network Failure..${e.message}"))
                    else -> emit(Resource.Error<Int>("Conversion Error..${e.message}"))
                }
            }
        }.flowOn(dispatcher)
    }

}