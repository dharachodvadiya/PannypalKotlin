package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.database.entity.Merchant
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.MerchantRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UpdateMerchantUseCase @Inject constructor(
    private val merchantRepository: MerchantRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher) {

    suspend fun updateData(merchant: Merchant) : Flow<Resource<Int>>{
        return flow{

            try {
                emit(Resource.Loading())
                val count = merchantRepository.update(merchant)

                if(count >0){
                    emit(Resource.Success(count))
                }else{
                    emit(Resource.Error("Fail to update Merchant"))
                }


            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

}