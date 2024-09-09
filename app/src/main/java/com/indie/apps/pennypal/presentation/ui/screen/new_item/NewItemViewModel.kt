package com.indie.apps.pennypal.presentation.ui.screen.new_item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.data.entity.MerchantData
import com.indie.apps.pennypal.data.entity.Payment
import com.indie.apps.pennypal.data.entity.toMerchantNameAndDetails
import com.indie.apps.pennypal.data.module.MerchantNameAndDetails
import com.indie.apps.pennypal.domain.usecase.AddMerchantDataUseCase
import com.indie.apps.pennypal.domain.usecase.GetMerchantDataFromIdUseCase
import com.indie.apps.pennypal.domain.usecase.GetMerchantFromIdUseCase
import com.indie.apps.pennypal.domain.usecase.GetPaymentFromIdUseCase
import com.indie.apps.pennypal.domain.usecase.GetUserProfileUseCase
import com.indie.apps.pennypal.domain.usecase.UpdateMerchantDataUseCase
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.util.ErrorMessage
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewItemViewModel @Inject constructor(
    private val addMerchantDataUseCase: AddMerchantDataUseCase,
    private val updateMerchantDataUseCase: UpdateMerchantDataUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getMerchantDataFromIdUseCase: GetMerchantDataFromIdUseCase,
    private val getMerchantFromIdUseCase: GetMerchantFromIdUseCase,
    private val getPaymentFromIdUseCase: GetPaymentFromIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val merchantEditId =
        savedStateHandle.get<String>(Util.PARAM_EDIT_MERCHANT_DATA_ID)?.toLong() ?: 0
    private var editMerchantData: MerchantData? = null

    val merchant = MutableStateFlow<MerchantNameAndDetails?>(null)

    val payment = MutableStateFlow<Payment?>(null)

    val received = MutableStateFlow(false)

    val enableButton = MutableStateFlow(true)

    val amount = MutableStateFlow(TextFieldState())
    val description = MutableStateFlow(TextFieldState())

    var merchantError = MutableStateFlow("")
    var paymentError = MutableStateFlow("")

    val uiState = MutableStateFlow<Resource<Unit>>(Resource.Loading())

    init {
        if (merchantEditId == 0L)
            loadUserData()
        else
            setEditData()
    }

    private fun loadUserData() {
        uiState.value = Resource.Loading()
        viewModelScope.launch {
            getUserProfileUseCase.loadData()
                .collect {
                    loadPaymentData(it.paymentId)
                    uiState.value = Resource.Success(Unit)
                }
        }
    }

    private fun setEditData() {
        viewModelScope.launch {
            try {
                getMerchantDataFromIdUseCase.getData(merchantEditId).collect { it ->
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
                                    editMerchantData!!.type == 1
                                amount.value.text =
                                    Util.getFormattedString(editMerchantData!!.amount)
                                description.value.text =
                                    editMerchantData!!.details.toString()

                                loadPaymentData(editMerchantData!!.paymentId)
                                var merchantJob: Job? = null
                                merchantJob = launch {
                                    getMerchantFromIdUseCase.getData(editMerchantData!!.merchantId)
                                        .collect {
                                            setMerchantData(it.toMerchantNameAndDetails())
                                            merchantJob?.cancel()
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
                uiState.value = Resource.Error(e.message ?: "unexpected")
            }
        }
    }

    private fun loadPaymentData(id: Long) {
        viewModelScope.launch {
            getPaymentFromIdUseCase.getData(id)
                .collect {
                    setPaymentData(it)
                }
        }
    }

    fun onReceivedChange(isReceived: Boolean) {
        received.value = isReceived
    }

    fun setMerchantData(data: MerchantNameAndDetails) {
        merchantError.value = ""
        merchant.value = data
    }

    fun setPaymentData(data: Payment) {
        paymentError.value = ""
        payment.value = data
    }

    fun addOrEditMerchantData(onSuccess: (Boolean, Long, Long) -> Unit) {
        if (enableButton.value) {
            enableButton.value = false
            if (amount.value.text.trim().isEmpty()) {
                amount.value.setError(ErrorMessage.AMOUNT_EMPTY)
                enableButton.value = true
            } else if (merchant.value == null) {
                merchantError.value = ErrorMessage.SELECT_MERCHANT
                enableButton.value = true
            } else if (payment.value == null) {
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
                                    onSuccess(false, it.data ?: -1, merchantData.merchantId)
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
                                    onSuccess(true, merchantData.id, merchantData.merchantId)
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