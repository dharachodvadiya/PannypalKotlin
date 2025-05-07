package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.database.db_entity.MerchantData
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.MerchantDataRepository
import com.indie.apps.pennypal.util.app_enum.Resource
import com.indie.apps.pennypal.util.app_enum.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetMerchantDataFromIdUseCase @Inject constructor(
    private val merchantDataRepository: MerchantDataRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun getData(id: Long): Flow<Resource<MerchantData>> {
        return flow {
            if (id != 0L) {
                try {
                    emit(Resource.Loading())
                    val merchantFromId = merchantDataRepository.getMerchantDataFromId(id)
                    emit(Resource.Success(merchantFromId))
                } catch (e: Throwable) {
                    emit(Resource.Error(handleException(e).message + ": ${e.message}"))
                }
            } else {
                emit(Resource.Error("Data Not Found"))
            }


        }.flowOn(dispatcher)
    }

}