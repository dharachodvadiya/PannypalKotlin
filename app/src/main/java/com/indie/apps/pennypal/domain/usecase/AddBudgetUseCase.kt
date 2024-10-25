package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.module.BudgetWithCategory
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.BudgetRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.handleException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun addData(budgetWithCategory: BudgetWithCategory): Flow<Resource<Long>> {
        return flow {
            try {
                emit(Resource.Loading())
                val count = budgetRepository.insert(budgetWithCategory)

                if (count > 0) {
                    emit(Resource.Success(count))
                } else {
                    emit(Resource.Error("Fail to insert Budget"))
                }


            } catch (e: Throwable) {
                emit(Resource.Error(handleException(e).message + ": ${e.message}"))
            }

        }.flowOn(dispatcher)
    }

}