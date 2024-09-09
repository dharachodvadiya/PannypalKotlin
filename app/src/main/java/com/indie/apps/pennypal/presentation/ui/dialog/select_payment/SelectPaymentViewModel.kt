package com.indie.apps.pennypal.presentation.ui.dialog.select_payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.domain.usecase.GetPaymentListWithModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SelectPaymentViewModel@Inject constructor(
    getPaymentListWithModeUseCase: GetPaymentListWithModeUseCase
) :ViewModel() {

    val paymentState = getPaymentListWithModeUseCase
        .loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())
}