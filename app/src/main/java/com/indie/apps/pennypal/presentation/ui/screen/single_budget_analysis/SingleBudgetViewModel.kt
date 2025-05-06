package com.indie.apps.pennypal.presentation.ui.screen.single_budget_analysis

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.data.database.enum.PeriodType
import com.indie.apps.pennypal.data.module.budget.BudgetWithCategory
import com.indie.apps.pennypal.data.module.category.CategoryAmount
import com.indie.apps.pennypal.domain.usecase.DeleteSingleBudgetDataUseCase
import com.indie.apps.pennypal.domain.usecase.GetCategoryWiseSpentAmountForPeriodUseCase
import com.indie.apps.pennypal.repository.BudgetRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class SingleBudgetViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val getCategoryWiseSpentAmountForPeriodUseCase: GetCategoryWiseSpentAmountForPeriodUseCase,
    private val deleteSingleBudgetDataUseCase: DeleteSingleBudgetDataUseCase,
    //userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    /* val currency = userRepository.getCurrency()
         .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), "$")*/
    private val budgetId =
        savedStateHandle.get<String>(Util.PARAM_BUDGET_ID)?.toLong() ?: 0
    var budgetData = MutableStateFlow<BudgetWithCategory?>(null)
    var spentCategoryData = MutableStateFlow<List<CategoryAmount>>(emptyList())

    val uiState = MutableStateFlow<Resource<Unit>>(Resource.Loading())

    fun loadBudgetData() {
        uiState.value = Resource.Loading()
        viewModelScope.launch {
            try {
                budgetRepository.getBudgetWithCategoryFromId(budgetId).collect {
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
            PeriodType.MONTH.id -> {
                val startCal: Calendar =
                    Calendar.getInstance().apply { timeInMillis = budget.startDate }
                getCategoryWiseSpentAmountForPeriodUseCase.loadCategoryWiseTotalAmountForMonth(
                    year = startCal.get(Calendar.YEAR),
                    month = startCal.get(Calendar.MONTH),
                    categoryIds = budget.category.map { it.id },
                    toCurrencyId = budget.originalCurrencyId
                ).collect {
                    spentCategoryData.value = it
                    uiState.value = Resource.Success(Unit)
                }
            }

            PeriodType.YEAR.id -> {
                val startCal: Calendar =
                    Calendar.getInstance().apply { timeInMillis = budget.startDate }
                getCategoryWiseSpentAmountForPeriodUseCase.loadCategoryWiseTotalAmountForYear(
                    year = startCal.get(Calendar.YEAR),
                    categoryIds = budget.category.map { it.id },
                    toCurrencyId = budget.originalCurrencyId
                ).collect {
                    spentCategoryData.value = it
                    uiState.value = Resource.Success(Unit)
                }
            }

            PeriodType.ONE_TIME.id -> getCategoryWiseSpentAmountForPeriodUseCase.loadCategoryWiseTotalAmountForBetweenDates(
                startTime = budget.startDate,
                endTime = budget.endDate ?: 0,
                categoryIds = budget.category.map { it.id },
                toCurrencyId = budget.originalCurrencyId
            ).collect {
                spentCategoryData.value = it
                uiState.value = Resource.Success(Unit)
            }


        }
    }

    fun onDeleteDialogClick(onSuccess: (Long) -> Unit) {

        onSuccess(budgetId)
        /*viewModelScope.launch {
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
        }*/

    }

}