package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.module.budget.BudgetWithCategory
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.BudgetRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UpdateBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun updateData(budgetWithCategory: BudgetWithCategory): Flow<Resource<Int>> {
        return flow {
            try {
                emit(Resource.Loading())
                val count = budgetRepository.update(budgetWithCategory)

                if (count > 0) {
                    emit(Resource.Success(count))
                } else {
                    emit(Resource.Error("Fail to update Budget"))
                }


            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }

        }.flowOn(dispatcher)
    }

}