package com.indie.apps.pennypal.presentation.ui.screen.overview_analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.domain.usecase.GetCategoryWiseExpenseFromPreferencePeriodUseCase
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.util.ShowDataPeriod
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class OverViewAnalysisViewModel @Inject constructor(
    getCategoryWiseExpenseFromMonthUseCase: GetCategoryWiseExpenseFromPreferencePeriodUseCase,
    preferenceRepository: PreferenceRepository,
) : ViewModel() {

    val monthlyCategoryExpense = getCategoryWiseExpenseFromMonthUseCase
        .loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    private val periodIndex = preferenceRepository.getInt(Util.PREF_BALANCE_VIEW, 1)
    val currentPeriod = MutableStateFlow(ShowDataPeriod.fromIndex(periodIndex))

}