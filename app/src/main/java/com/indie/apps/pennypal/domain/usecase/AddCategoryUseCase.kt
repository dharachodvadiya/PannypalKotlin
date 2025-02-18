package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.database.entity.Category
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.CategoryRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun addCategory(category: Category): Flow<Resource<Long>> {
        return flow {
            emit(Resource.Loading())
            try {
                val id = categoryRepository.insert(category)
                if (id > 0) {
                    emit(Resource.Success(id))
                } else {
                    emit(Resource.Error("Fail to add category"))
                }

            } catch (e: Throwable) {

                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }

}