package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.entity.Merchant
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.MerchantDataRepository
import com.indie.apps.pennypal.repository.MerchantRepository
import com.indie.apps.pennypal.repository.UserRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DeleteSingleMerchantUseCase @Inject constructor(
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