package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.database.db_entity.Merchant
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.MerchantRepository
import com.indie.apps.pennypal.util.app_enum.Resource
import com.indie.apps.pennypal.util.app_enum.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddMerchantUseCase @Inject constructor(
    private val merchantRepository: MerchantRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    fun addMerchant(merchant: Merchant): Flow<Resource<Long>> {
        return flow {
            emit(Resource.Loading())
            try {
                val id = merchantRepository.insert(merchant)
                if (id > 0) {
                    emit(Resource.Success(id))
                } else {
                    emit(Resource.Error("Fail to add Merchant"))
                }

            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

}