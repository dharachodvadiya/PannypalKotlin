package com.indie.apps.pennypal.presentation.ui.screen.budget_filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.data.database.enum.PeriodType
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIdList
import com.indie.apps.pennypal.domain.usecase.GetBudgetFromPeriodUseCase
import com.indie.apps.pennypal.domain.usecase.GetSpentAmountForPeriodAndCategoryUseCase
import com.indie.apps.pennypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class BudgetFilterViewModel @Inject constructor(
) : ViewModel() {

    val currentPeriod = MutableStateFlow(PeriodType.MONTH.id)

    fun setCurrentPeriod(periodType: PeriodType) {
        currentPeriod.value = periodType.id
    }
}