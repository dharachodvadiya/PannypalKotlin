package com.indie.apps.pannypal.domain.usecase

import android.net.http.HttpException
import androidx.compose.ui.unit.Constraints
import com.indie.apps.pannypal.data.dao.MerchantDataDao
import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.data.module.MerchantDataWithName
import com.indie.apps.pannypal.repository.MerchantDataRepository
import com.indie.apps.pannypal.repository.MerchantRepository
import com.indie.apps.pannypal.repository.UserRepository
import com.indie.apps.pannypal.util.Constant
import com.indie.apps.pannypal.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class addMerchantDataUsecase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    private val merchantRepository: MerchantRepository,
    private val userRepository: UserRepository,
    private val merchantData: MerchantData,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend operator fun invoke() : Flow<Resource<Long>>{
        return flow{

            try {
                emit(Resource.Loading<Long>())
                val id = merchantDataRepository.insert(merchantData)

                if(id >0)
                {
                    merchantData.run {
                        val updatedRowMerchant = merchantRepository.updateAmountWithDate(
                            id = merchantId,
                            incomeAmt = if(amount >= 0) amount else 0,
                            expenseAmt = if(amount <0) (amount * -1) else 0,
                            System.currentTimeMillis()
                        )

                        val updatedRowUser = userRepository.updateAmount(
                            incomeAmt = if(amount >= 0) amount else 0,
                            expenseAmt = if(amount <0) (amount * -1) else 0
                        )

                        when {
                            updatedRowUser == 1 && updatedRowMerchant == 1 -> {
                                emit(Resource.Success<Long>(id))
                            }
                            updatedRowUser == 1 && updatedRowMerchant == 0 -> {
                                emit(Resource.Error<Long>("Merchant not updated"))
                            }
                            updatedRowUser == 0 && updatedRowMerchant == 1 -> {
                                emit(Resource.Error<Long>("User not updated"))
                            }
                            else -> {
                                emit(Resource.Error<Long>("User and Merchant not updated"))
                            }
                        }
                    }
                }else{
                    emit(Resource.Error<Long>("Merchant data not updated"))
                }


            } catch(e: Throwable) {
                when(e) {
                    is IOException -> emit(Resource.Error<Long>("Network Failure"))
                    else -> emit(Resource.Error<Long>("Conversion Error"))
                }
            }
        }.flowOn(dispatcher)
    }

}