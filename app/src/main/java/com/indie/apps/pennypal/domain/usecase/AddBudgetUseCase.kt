package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.data.module.budget.BudgetWithCategory
import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.BudgetRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.Util
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
    fun addData(
        budgetWithCategory: BudgetWithCategory,
        year: Int,
        month: Int
    ): Flow<Resource<Long>> {
        return flow {
            try {
                emit(Resource.Loading())
                val count = budgetRepository.insertBudgetWithPeriodValidation(
                    obj = budgetWithCategory,
                    year = year,
                    month = month,
                    timeZoneOffsetInMilli = Util.TIME_ZONE_OFFSET_IN_MILLI
                )

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