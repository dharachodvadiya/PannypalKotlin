package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.BudgetRepository
import com.indie.apps.pennypal.util.app_enum.Resource
import com.indie.apps.pennypal.util.app_enum.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DeleteSingleBudgetDataUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun deleteBudgetFromId(id: Long): Flow<Resource<Int>> {
        return flow {

            try {
                emit(Resource.Loading())
                val count = budgetRepository.deleteBudget(id)

                if (count > 0) {
                    emit(Resource.Success(count))
                } else {
                    emit(Resource.Error("Fail to delete Single Budget Data"))
                }

            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }
        }.flowOn(dispatcher)
    }
}