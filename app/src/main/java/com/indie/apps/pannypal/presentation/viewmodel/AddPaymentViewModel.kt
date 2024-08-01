package com.indie.apps.pannypal.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.domain.usecase.AddPaymentUseCase
import com.indie.apps.pannypal.presentation.ui.state.TextFieldState
import com.indie.apps.pannypal.util.ErrorMessage
import com.indie.apps.pannypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPaymentViewModel @Inject constructor(private val addPaymentUseCase: AddPaymentUseCase) :
    ViewModel() {

    val paymentTypeState by mutableStateOf(TextFieldState())

    fun addPayment(onSuccess: ()-> Unit) {

        viewModelScope.launch {
            addPaymentUseCase
                .addPayment(Payment(name = paymentTypeState.text.trim()))
                .collect {
                    when(it)
                    {
                        is Resource.Loading -> {}
                        is Resource.Success -> onSuccess()
                        is Resource.Error -> {
                            paymentTypeState.setError(ErrorMessage.PAYMENT_TYPE_EXIST)
                        }
                    }
                }
        }
    }

}