package com.indie.apps.pannypal.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.data.entity.toMerchantNameAndDetails
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails
import com.indie.apps.pannypal.domain.usecase.AddMerchantDataUseCase
import com.indie.apps.pannypal.domain.usecase.GetMerchantDataFromIdUseCase
import com.indie.apps.pannypal.domain.usecase.GetMerchantFromIdUseCase
import com.indie.apps.pannypal.domain.usecase.GetPaymentFromIdUseCase
import com.indie.apps.pannypal.domain.usecase.GetPaymentListUseCase
import com.indie.apps.pannypal.domain.usecase.UpdateMerchantDataUseCase
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.state.TextFieldState
import com.indie.apps.pannypal.util.ErrorMessage
import com.indie.apps.pannypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewItemViewModel @Inject constructor(
    private val addMerchantDataUseCase: AddMerchantDataUseCase,
    private val updateMerchantDataUseCase: UpdateMerchantDataUseCase,
    getPaymentListUseCase: GetPaymentListUseCase,
    private val getMerchantDataFromIdUseCase: GetMerchantDataFromIdUseCase,
    private val getMerchantFromIdUseCase: GetMerchantFromIdUseCase,
    private val getPaymentFromIdUseCase: GetPaymentFromIdUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val merchantEditId =
        savedStateHandle?.get<String>(Util.PARAM_EDIT_MERCHANT_DATA_ID)?.toLong() ?: 0
    private var editMerchantData: MerchantData? = null

    val merchant = MutableStateFlow<MerchantNameAndDetails?>(null)

    val payment = MutableStateFlow<Payment?>(null)

    val received = MutableStateFlow(false)

    val enableButton = MutableStateFlow(true)

    val amount = MutableStateFlow(TextFieldState())
    val description = MutableStateFlow(TextFieldState())

    var merchantError = MutableStateFlow("")
    var paymentError = MutableStateFlow("")

    val paymentList = getPaymentListUseCase.loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val uiState = MutableStateFlow<Resource<Unit>>(Resource.Loading())

    init {
        setEditData()
    }

    fun setEditData() {
        if (merchantEditId != 0L) {
            viewModelScope.launch {

                try {
                    getMerchantDataFromIdUseCase.getData(merchantEditId).collect {
                            when (it) {
                                is Resource.Loading -> {
                                    uiState.value = Resource.Loading()
                                }

                                is Resource.Error -> {
                                    uiState.value = Resource.Error("")
                                }

                                is Resource.Success -> {
                                    if (it.data != null) {
                                        editMerchantData = it.data
                                        received.value =
                                            if (editMerchantData!!.type == 1) true else false
                                        amount.value.text =
                                            Util.getFormattedString(editMerchantData!!.amount)
                                        description.value.text =
                                            editMerchantData!!.details.toString()

                                        launch {
                                            getPaymentFromIdUseCase.getData(editMerchantData!!.paymentId)
                                                .collect {
                                                    if (it is Resource.Success && it.data != null) {
                                                        setPaymentData(it.data)
                                                    }
                                                }
                                        }

                                        launch {
                                            getMerchantFromIdUseCase.getData(editMerchantData!!.merchantId)
                                                .collect {
                                                    if (it != null) {
                                                        setMerchantData(it.toMerchantNameAndDetails())
                                                    }
                                                }
                                        }

                                        uiState.value = Resource.Success(Unit)
                                    } else {
                                        uiState.value = Resource.Error("Not found")
                                    }
                                }
                            }
                        }
                } catch (e: Exception) {
                    uiState.value = Resource.Error("${e.localizedMessage}")
                }
            }
        } else {
            uiState.value = Resource.Success(Unit)
        }
    }

    fun onReceivedChange(isReceived: Boolean) {
        received.value = isReceived
    }

    fun onPaymentSelect(data: Payment) {
        paymentError.value = ""
        payment.value = data
    }

    fun setMerchantData(data: MerchantNameAndDetails) {
        merchantError.value = ""
        merchant.value = data
    }

    fun setPaymentData(data: Payment) {
        paymentError.value = ""
        payment.value = data
    }

    fun addOrEditMerchantData(onSuccess: (Boolean) -> Unit) {
        if (enableButton.value) {
            enableButton.value = false
            if (amount.value.text.trim().isNullOrEmpty()) {
                amount.value.setError(ErrorMessage.AMOUNT_EMPTY)
                enableButton.value = true
            } else if (merchant == null) {
                merchantError.value = ErrorMessage.SELECT_MERCHANT
                enableButton.value = true
            } else if (payment == null) {
                paymentError.value = ErrorMessage.SELECT_PAYMENT
                enableButton.value = true
            } else {
                val amount = amount.value.text.toDouble()

                if (merchantEditId == 0L) {
                    val merchantData = MerchantData(
                        merchantId = merchant.value!!.id,
                        paymentId = payment.value!!.id,
                        amount = amount,
                        details = description.value.text.trim(),
                        dateInMilli = System.currentTimeMillis(),
                        type = if (received.value) 1 else -1
                    )

                    viewModelScope.launch {
                        addMerchantDataUseCase.addData(merchantData).collect {
                                when (it) {
                                    is Resource.Loading -> {}
                                    is Resource.Success -> {
                                        enableButton.value = true
                                        onSuccess(false)
                                    }

                                    is Resource.Error -> {
                                        enableButton.value = true
                                    }
                                }
                            }
                    }
                } else {
                    val merchantData = editMerchantData!!.copy(
                        merchantId = merchant.value!!.id,
                        paymentId = payment.value!!.id,
                        amount = amount,
                        details = description.value.text.trim(),
                        type = if (received.value) 1 else -1
                    )

                    viewModelScope.launch {
                        updateMerchantDataUseCase.updateData(
                                merchantDataNew = merchantData, merchantDataOld = editMerchantData!!
                            ).collect {
                                when (it) {
                                    is Resource.Loading -> {}
                                    is Resource.Success -> {
                                        enableButton.value = true
                                        onSuccess(true)
                                    }

                                    is Resource.Error -> {
                                        enableButton.value = true
                                    }
                                }
                            }
                    }
                }
            }
        }


    }

}