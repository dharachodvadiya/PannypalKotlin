package com.indie.apps.pennypal.presentation.ui.screen.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.data.database.enum.PeriodType
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIdList
import com.indie.apps.pennypal.repository.BudgetRepository
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    budgetRepository: BudgetRepository,
) : ViewModel() {

    private val calendar: Calendar = Calendar.getInstance()

    val monthlyBudgets = MutableStateFlow<List<BudgetWithSpentAndCategoryIdList>>(emptyList())
    val yearlyBudgets = MutableStateFlow<List<BudgetWithSpentAndCategoryIdList>>(emptyList())
    val oneTimeBudgets = MutableStateFlow<List<BudgetWithSpentAndCategoryIdList>>(emptyList())

    private val budgetState = budgetRepository.getBudgetsAndSpentWithCategoryIdListFromMonth(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH),
        timeZoneOffsetInMilli = Util.TIME_ZONE_OFFSET_IN_MILLI
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

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
                        PeriodType.MONTH.id -> monthList.add(budget) // Period type 1 = Monthly
                        PeriodType.YEAR.id -> yearList.add(budget)  // Period type 2 = Yearly
                        PeriodType.ONE_TIME.id -> oneTimeList.add(budget) // Period type 3 = One-time
                    }
                }

                monthlyBudgets.value = monthList
                yearlyBudgets.value = yearList
                oneTimeBudgets.value = oneTimeList

            }
        }
    }

}