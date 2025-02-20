package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.CategoryRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DeleteMultipleCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun deleteData(ids: List<Long>): Flow<Resource<Int>> {
        return flow {

            try {
                emit(Resource.Loading())
                val merchantDeleteCount = categoryRepository.deleteMultipleCategory(ids)

                if (merchantDeleteCount == ids.size) {

                    emit(Resource.Success(merchantDeleteCount))

                } else {
                    emit(Resource.Error("Fail to delete multiple category"))
                }


            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

}