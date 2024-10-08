package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.entity.Merchant
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.MerchantRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetMerchantFromIdUseCase @Inject constructor(
    private val merchantRepository: MerchantRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    /* fun getData(id: Long) : Flow<Resource<Merchant>>{
         return flow{
             if(id != 0L)
             {
                 try {
                     emit(Resource.Loading())
                     val merchantFromId = merchantRepository.getMerchantFromId(id)
                     emit(Resource.Success(merchantFromId))
                 }catch (e: Throwable) {
                     emit(Resource.Error(handleException(e).message + ": ${e.message}"))
                 }
             }else{
                 emit(Resource.Error("Data Not Found"))
             }


         }.flowOn(dispatcher)
     }*/

    fun getData(id: Long): Flow<Merchant> {
        return merchantRepository.getMerchantFromId(id).flowOn(dispatcher)
    }

}