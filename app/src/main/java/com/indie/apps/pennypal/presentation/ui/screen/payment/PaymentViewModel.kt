package com.indie.apps.pennypal.presentation.ui.screen.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.domain.usecase.GetPaymentListWithModeUseCase
import com.indie.apps.pennypal.domain.usecase.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    userProfileUseCase: GetUserProfileUseCase,
    getPaymentListWithModeUseCase: GetPaymentListWithModeUseCase,
) : ViewModel(){

    val userState = userProfileUseCase
        .loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    val paymentWithModeState = getPaymentListWithModeUseCase.loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val isInEditMode = MutableStateFlow(false)

    fun setEditMode(isEditable: Boolean)
    {
        isInEditMode.value = isEditable
    }

    fun saveEditedData()
    {
        isInEditMode.value = false
    }

}