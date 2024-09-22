package com.indie.apps.pennypal.presentation.ui.screen.overview_analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.domain.usecase.GetCategoryWiseExpenseFromMonthUseCase
import com.indie.apps.pennypal.domain.usecase.GetTotalFromMonthUseCase
import com.indie.apps.pennypal.domain.usecase.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class OverViewAnalysisViewModel @Inject constructor(
    userProfileUseCase: GetUserProfileUseCase,
    getTotalFromMonthUseCase: GetTotalFromMonthUseCase,
    getCategoryWiseExpenseFromMonthUseCase: GetCategoryWiseExpenseFromMonthUseCase,
) : ViewModel() {

    val userData = userProfileUseCase.loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    val monthlyCategoryExpense = getCategoryWiseExpenseFromMonthUseCase
        .loadData(0)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val currentMonthTotal = getTotalFromMonthUseCase.loadData(0)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

}