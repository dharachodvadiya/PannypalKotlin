package com.indie.apps.pannypal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.domain.usecase.AddPaymentUseCase
import com.indie.apps.pannypal.presentation.ui.state.TextFieldState
import com.indie.apps.pannypal.util.ErrorMessage
import com.indie.apps.pannypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPaymentViewModel @Inject constructor(private val addPaymentUseCase: AddPaymentUseCase) :
    ViewModel() {

    val paymentTypeState = MutableStateFlow(TextFieldState())
    val enableButton = MutableStateFlow(true)

    fun addPayment(onSuccess: (Payment?) -> Unit) {

        if (enableButton.value) {
            enableButton.value = false
            if (paymentTypeState.value.text.trim().isEmpty()) {
                paymentTypeState.value.setError(ErrorMessage.AMOUNT_PAYMENT_TYPE)
                enableButton.value = true
            } else {
                viewModelScope.launch {
                    val payment = Payment(name = paymentTypeState.value.text.trim())
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

                                    enableButton.value = true
                                }

                                is Resource.Error -> {
                                    paymentTypeState.value.setError(ErrorMessage.PAYMENT_TYPE_EXIST)
                                    enableButton.value = true
                                }
                            }
                        }
                }
            }
        }

    }

}