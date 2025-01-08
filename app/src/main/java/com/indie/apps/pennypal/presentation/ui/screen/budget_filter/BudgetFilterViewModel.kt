package com.indie.apps.pennypal.presentation.ui.screen.budget_filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.indie.apps.pennypal.data.database.enum.BudgetMenu
import com.indie.apps.pennypal.data.database.enum.PeriodType
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIdList
import com.indie.apps.pennypal.domain.usecase.GetPastBudgetsAndSpentWithCategoryIdListFromPeriodType
import com.indie.apps.pennypal.domain.usecase.GetUpComingBudgetsAndSpentWithCategoryIdListFromPeriodType
import com.indie.apps.pennypal.presentation.ui.state.PagingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class BudgetFilterViewModel @Inject constructor(
    getPastBudgetsAndSpentWithCategoryIdListFromPeriodType: GetPastBudgetsAndSpentWithCategoryIdListFromPeriodType,
    getUpComingBudgetsAndSpentWithCategoryIdListFromPeriodType: GetUpComingBudgetsAndSpentWithCategoryIdListFromPeriodType
) : ViewModel() {
    private val calendar: Calendar = Calendar.getInstance()
    val currentPeriod = MutableStateFlow(PeriodType.MONTH.id)
    private val currentFilter = MutableStateFlow(BudgetMenu.PAST.id)
    private val trigger = MutableSharedFlow<Unit>(replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedData = trigger
        .flatMapLatest {
            when (currentFilter.value) {
                BudgetMenu.PAST.id -> {
                    getPastBudgetsAndSpentWithCategoryIdListFromPeriodType.loadData(
                        year = calendar.get(Calendar.YEAR),
                        month = calendar.get(Calendar.MONTH),
                        periodType = currentPeriod.value
                    )
                }

                BudgetMenu.UPCOMING.id -> {
                    getUpComingBudgetsAndSpentWithCategoryIdListFromPeriodType.loadData(
                        year = calendar.get(Calendar.YEAR),
                        month = calendar.get(Calendar.MONTH),
                        periodType = currentPeriod.value
                    )
                }

                else -> {
                    getPastBudgetsAndSpentWithCategoryIdListFromPeriodType.loadData(
                        year = calendar.get(Calendar.YEAR),
                        month = calendar.get(Calendar.MONTH),
                        periodType = currentPeriod.value
                    )
                }
            }

        }
        .cachedIn(viewModelScope)

    val pagingState = MutableStateFlow(PagingState<BudgetWithSpentAndCategoryIdList>())

    fun setCurrentPeriod(periodId: Int) {
        currentPeriod.value = periodId
        viewModelScope.launch {
            trigger.emit(Unit)
        }
    }

    fun setFilterId(filterId: Int) {
        currentFilter.value = filterId
        viewModelScope.launch {
            trigger.emit(Unit)
        }
    }
}