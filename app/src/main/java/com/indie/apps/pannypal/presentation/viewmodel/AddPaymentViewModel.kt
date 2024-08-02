package com.indie.apps.pannypal.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    var enableButton by (mutableStateOf(true))
        private set

    fun addPayment(onSuccess: (Payment?) -> Unit) {

        if (enableButton) {
            enableButton = false
            if (paymentTypeState.text.trim().isNullOrEmpty()) {
                paymentTypeState.setError(ErrorMessage.AMOUNT_PAYMENT_TYPE)
                enableButton = true
            } else {
                viewModelScope.launch {
                    val payment = Payment(name = paymentTypeState.text.trim())
                    addPaymentUseCase
                        .addPayment(payment)
                        .collect {
                            when (it) {
                                is Resource.Loading -> {}
                                is Resource.Success -> {
                                    onSuccess(it.data?.let { it1 ->
                                        payment.copy(
                                            id = it1
                                        )
                                    })

                                    enableButton = true
                                }

                                is Resource.Error -> {
                                    paymentTypeState.setError(ErrorMessage.PAYMENT_TYPE_EXIST)
                                    enableButton = true
                                }
                            }
                        }
                }
            }
        }

    }

}