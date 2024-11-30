package com.indie.apps.pennypal.presentation.ui.screen.single_budget_analysis

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.data.database.enum.BudgetPeriodType
import com.indie.apps.pennypal.data.module.budget.BudgetWithCategory
import com.indie.apps.pennypal.data.module.category.CategoryAmount
import com.indie.apps.pennypal.domain.usecase.DeleteSingleBudgetDataUseCase
import com.indie.apps.pennypal.domain.usecase.GetBudgetWithCategoryFromBudgetIdUseCase
import com.indie.apps.pennypal.domain.usecase.GetCategoryWiseSpentAmountForPeriodUseCase
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class SingleBudgetViewModel @Inject constructor(
    private val getBudgetWithCategoryFromBudgetIdUseCase: GetBudgetWithCategoryFromBudgetIdUseCase,
    private val getCategoryWiseSpentAmountForPeriodUseCase: GetCategoryWiseSpentAmountForPeriodUseCase,
    private val deleteSingleBudgetDataUseCase: DeleteSingleBudgetDataUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val budgetId =
        savedStateHandle.get<String>(Util.PARAM_BUDGET_ID)?.toLong() ?: 0
    var budgetData = MutableStateFlow<BudgetWithCategory?>(null)
    var spentCategoryData = MutableStateFlow<List<CategoryAmount>>(emptyList())

    val uiState = MutableStateFlow<Resource<Unit>>(Resource.Loading())

    fun loadBudgetData() {
        uiState.value = Resource.Loading()
        viewModelScope.launch {
            try {
                getBudgetWithCategoryFromBudgetIdUseCase.loadData(budgetId).collect {
                    //budgetData = it
                    budgetData.value = it
                    loadCategoryWiseSpentData(it)
                }
            } catch (e: Exception) {
                uiState.value = Resource.Error("")
            }
        }
    }

    private suspend fun loadCategoryWiseSpentData(budget: BudgetWithCategory) {
        when (budget.periodType) {
            BudgetPeriodType.MONTH.id -> {
                val startCal: Calendar =
                    Calendar.getInstance().apply { timeInMillis = budget.startDate }
                getCategoryWiseSpentAmountForPeriodUseCase.loadCategoryWiseTotalAmountForMonth(
                    year = startCal.get(Calendar.YEAR),
                    month = startCal.get(Calendar.MONTH),
                    categoryIds = budget.category.map { it.id }
                ).collect {
                    spentCategoryData.value = it
                    uiState.value = Resource.Success(Unit)
                }
            }

            BudgetPeriodType.YEAR.id -> {
                val startCal: Calendar =
                    Calendar.getInstance().apply { timeInMillis = budget.startDate }
                getCategoryWiseSpentAmountForPeriodUseCase.loadCategoryWiseTotalAmountForYear(
                    year = startCal.get(Calendar.YEAR), categoryIds = budget.category.map { it.id }
                ).collect {
                    spentCategoryData.value = it
                    uiState.value = Resource.Success(Unit)
                }
            }

            BudgetPeriodType.ONE_TIME.id -> getCategoryWiseSpentAmountForPeriodUseCase.loadCategoryWiseTotalAmountForBetweenDates(
                startTime = budget.startDate,
                endTime = budget.endDate ?: 0,
                categoryIds = budget.category.map { it.id }
            ).collect {
                spentCategoryData.value = it
                uiState.value = Resource.Success(Unit)
            }


        }
    }

    fun onDeleteDialogClick(onSuccess: (Long) -> Unit) {
        viewModelScope.launch {
            deleteSingleBudgetDataUseCase
                .deleteBudgetFromId(budgetId)
                .collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            onSuccess(budgetId)
                        }

                        is Resource.Error -> {
                        }
                    }
                }
        }

    }

}