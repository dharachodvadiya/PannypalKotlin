package com.indie.apps.pennypal.presentation.ui.screen.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIdList
import com.indie.apps.pennypal.domain.usecase.GetBudgetFromPeriodUseCase
import com.indie.apps.pennypal.domain.usecase.GetSpentAmountForPeriodAndCategoryUseCase
import com.indie.apps.pennypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    getBudgetFromPeriodUseCase: GetBudgetFromPeriodUseCase,
    private val getSpentAmountForPeriodAndCategoryUseCase: GetSpentAmountForPeriodAndCategoryUseCase
) : ViewModel() {
    private val calendar: Calendar = Calendar.getInstance()

    val monthlyBudgets = MutableStateFlow<List<BudgetWithSpentAndCategoryIdList>>(emptyList())
    val yearlyBudgets = MutableStateFlow<List<BudgetWithSpentAndCategoryIdList>>(emptyList())
    val oneTimeBudgets = MutableStateFlow<List<BudgetWithSpentAndCategoryIdList>>(emptyList())

    private val budgetState = getBudgetFromPeriodUseCase.loadFromMonth(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH)
    ).flatMapConcat { budgets ->  // Sequentially process each budget
        flow {
            val budgetWithSpentList = mutableListOf<BudgetWithSpentAndCategoryIdList>()

            // Process each budget one by one
            for (budget in budgets) {

                when (budget.periodType) {
                    1 -> {
                        getSpentAmountForPeriodAndCategoryUseCase.loadTotalAmountForMonth(
                            year = calendar.get(Calendar.YEAR),
                            month = calendar.get(Calendar.MONTH),
                            categoryIds = budget.category
                        ).collect { resource ->
                            when (resource) {
                                is Resource.Loading -> 0.0
                                is Resource.Success -> {
                                    budgetWithSpentList.add(
                                        budget.copy(
                                            spentAmount = resource.data ?: 0.0
                                        )
                                    )
                                }

                                is Resource.Error -> 0.0
                            }

                        }
                    }

                    2 -> {
                        getSpentAmountForPeriodAndCategoryUseCase.loadTotalAmountForYear(
                            year = calendar.get(Calendar.YEAR), categoryIds = budget.category
                        ).collect { resource ->
                            when (resource) {
                                is Resource.Loading -> 0.0
                                is Resource.Success -> {
                                    budgetWithSpentList.add(
                                        budget.copy(
                                            spentAmount = resource.data ?: 0.0
                                        )
                                    )
                                }

                                is Resource.Error -> 0.0
                            }
                        }
                    }

                    3 -> getSpentAmountForPeriodAndCategoryUseCase.loadTotalAmountForBetweenDates(
                        startTime = budget.startDate,
                        endTime = budget.endDate ?: 0,
                        categoryIds = budget.category
                    ).collect { resource ->
                        when (resource) {
                            is Resource.Loading -> 0.0
                            is Resource.Success -> {
                                budgetWithSpentList.add(
                                    budget.copy(
                                        spentAmount = resource.data ?: 0.0
                                    )
                                )
                            }

                            is Resource.Error -> 0.0
                        }
                    }
                }
            }
            // Emit the list once all items are processed
            emit(budgetWithSpentList)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    init {
        loadBudgetData()
    }

    private fun loadBudgetData() {
        viewModelScope.launch {
            budgetState.collect { budgets ->
                val monthList = mutableListOf<BudgetWithSpentAndCategoryIdList>()
                val yearList = mutableListOf<BudgetWithSpentAndCategoryIdList>()
                val oneTimeList = mutableListOf<BudgetWithSpentAndCategoryIdList>()

                // Separate based on period_type
                budgets.forEach { budget ->
                    when (budget.periodType) {
                        1 -> monthList.add(budget) // Period type 1 = Monthly
                        2 -> yearList.add(budget)  // Period type 2 = Yearly
                        3 -> oneTimeList.add(budget) // Period type 3 = One-time
                    }
                }

                monthlyBudgets.value = monthList
                yearlyBudgets.value = yearList
                oneTimeBudgets.value = oneTimeList

            }
        }
    }

}