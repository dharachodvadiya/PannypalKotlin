package com.indie.apps.pannypal.domain.usecase

import android.util.Log
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.di.IoDispatcher
import com.indie.apps.pannypal.repository.MerchantRepository
import com.indie.apps.pannypal.util.Resource
import com.indie.apps.pannypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddMerchantUseCase @Inject constructor(
    private val merchantRepository: MerchantRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun addMerchant(merchant: Merchant) : Flow<Resource<Long>>{
        return flow{

            try {
                val id = merchantRepository.insert(merchant)
                if(id >0){
                    emit(Resource.Success(id))
                }else{
                    emit(Resource.Error("Fail to add Merchant"))
                }

            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

}