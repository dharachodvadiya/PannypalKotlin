package com.indie.apps.pennypal.domain.usecase

import com.indie.apps.pennypal.di.IoDispatcher
import com.indie.apps.pennypal.repository.BudgetRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetBudgetWithCategoryFromBudgetIdUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    fun loadData(budgetId: Long) = budgetRepository.getBudgetWithCategoryFromId(budgetId)
        .flowOn(dispatcher)

}