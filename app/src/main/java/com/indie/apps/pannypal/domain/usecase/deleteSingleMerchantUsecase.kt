package com.indie.apps.pannypal.domain.usecase

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

class deleteSingleMerchantUsecase @Inject constructor(
    private val merchantRepository: MerchantRepository,
    private val merchantDataRepository: MerchantDataRepository,
    private val userRepository: UserRepository,
    private val merchant: Merchant,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend operator fun invoke() : Flow<Resource<Int>>{
        return flow{

            try {
                emit(Resource.Loading())
                val merchantDeleteCount = merchantRepository.deleteMerchantWithId(merchant.id)

                if(merchantDeleteCount > 0){
                    merchantDataRepository.deleteMerchantDataWithMerchantId(merchant.id)
                    val updatedRowUser = userRepository.updateAmount(-merchant.incomeAmount, -merchant.expenseAmount)

                    emit(
                        when {
                            updatedRowUser == 1 -> Resource.Success(merchantDeleteCount)
                            else -> Resource.Error("Fail to update User Amount")
                        }
                    )

                }else{
                    emit(Resource.Error("Fail to delete single Merchant"))
                }

            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

}