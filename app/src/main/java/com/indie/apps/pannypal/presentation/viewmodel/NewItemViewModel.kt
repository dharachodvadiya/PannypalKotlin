package com.indie.apps.pannypal.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails
import com.indie.apps.pannypal.domain.usecase.AddMerchantDataUseCase
import com.indie.apps.pannypal.domain.usecase.GetPaymentListUseCase
import com.indie.apps.pannypal.presentation.ui.state.TextFieldState
import com.indie.apps.pannypal.util.ErrorMessage
import com.indie.apps.pannypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewItemViewModel @Inject constructor(
    private val addMerchantDataUseCase: AddMerchantDataUseCase,
    getPaymentListUseCase: GetPaymentListUseCase
) :
    ViewModel() {

    var merchant: MerchantNameAndDetails? by mutableStateOf(null)
        private set

    var payment: Payment? by mutableStateOf(null)
        private set

    var received by (mutableStateOf(false))
        private set

    var enableButton by (mutableStateOf(true))
        private set

    val amount by mutableStateOf(TextFieldState())
    val description by mutableStateOf(TextFieldState())

    var merchantError: String by mutableStateOf("")
    var paymentError: String by mutableStateOf("")

    val paymentList = getPaymentListUseCase.loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    fun onReceivedChange(isReceived: Boolean) {
        received = isReceived
    }

    fun onPaymentSelect(data: Payment) {
        paymentError = ""
        payment = data
    }

    fun setMerchantData(data: MerchantNameAndDetails) {
        merchantError = ""
        merchant = data
    }

    fun setPaymentData(data: Payment) {
        paymentError = ""
        payment = data
    }

    fun addMerchantData(onSuccess: () -> Unit) {
        if (enableButton) {
            enableButton = false
            if (amount.text.trim().isNullOrEmpty()) {
                amount.setError(ErrorMessage.AMOUNT_EMPTY)
                enableButton = true
            } else if (merchant == null) {
                merchantError = ErrorMessage.SELECT_MERCHANT
                enableButton = true
            } else if (payment == null) {
                paymentError = ErrorMessage.SELECT_PAYMENT
                enableButton = true
            } else {
                val amount = amount.text.toDouble()
                val merchantData = MerchantData(
                    merchantId = merchant!!.id,
                    paymentId = payment!!.id,
                    amount = amount,
                    details = description.text.trim(),
                    dateInMilli = System.currentTimeMillis(),
                    type = if(received) 1 else -1
                )

                viewModelScope.launch {
                    addMerchantDataUseCase
                        .addData(merchantData)
                        .collect {
                            when (it) {
                                is Resource.Loading -> {}
                                is Resource.Success -> {
                                    enableButton = true
                                    onSuccess()
                                }

                                is Resource.Error -> {
                                    enableButton = true
                                }
                            }
                        }
                }
            }
        }


    }

}