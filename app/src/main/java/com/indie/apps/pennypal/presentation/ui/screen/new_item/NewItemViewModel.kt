package com.indie.apps.pennypal.presentation.ui.screen.new_item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.data.database.entity.Category
import com.indie.apps.pennypal.data.database.entity.MerchantData
import com.indie.apps.pennypal.data.database.entity.Payment
import com.indie.apps.pennypal.data.database.entity.toMerchantNameAndDetails
import com.indie.apps.pennypal.data.module.MerchantNameAndDetails
import com.indie.apps.pennypal.domain.usecase.AddMerchantDataUseCase
import com.indie.apps.pennypal.domain.usecase.GetCategoryFromIdUseCase
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
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class NewItemViewModel @Inject constructor(
    private val addMerchantDataUseCase: AddMerchantDataUseCase,
    private val updateMerchantDataUseCase: UpdateMerchantDataUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getMerchantDataFromIdUseCase: GetMerchantDataFromIdUseCase,
    private val getMerchantFromIdUseCase: GetMerchantFromIdUseCase,
    private val getPaymentFromIdUseCase: GetPaymentFromIdUseCase,
    private val getCategoryFromIdUseCase: GetCategoryFromIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val merchantEditId =
        savedStateHandle.get<String>(Util.PARAM_EDIT_MERCHANT_DATA_ID)?.toLong() ?: 0
    private var editMerchantData: MerchantData? = null

    val merchant = MutableStateFlow<MerchantNameAndDetails?>(null)

    val payment = MutableStateFlow<Payment?>(null)

    val category = MutableStateFlow<Category?>(null)
    private var categoryIncome: Category? = null
    private var categoryExpense: Category? = null

    val received = MutableStateFlow(false)

    val enableButton = MutableStateFlow(true)

    val amount = MutableStateFlow(TextFieldState())
    val description = MutableStateFlow(TextFieldState())

    val currentTimeInMilli = MutableStateFlow(0L)

    var merchantError = MutableStateFlow("")
    var paymentError = MutableStateFlow("")
    var categoryError = MutableStateFlow("")

    val uiState = MutableStateFlow<Resource<Unit>>(Resource.Loading())

    init {
        if (merchantEditId == 0L) {
            loadUserData()
            setDateAndTime(Calendar.getInstance().timeInMillis)
        } else
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

    fun isEditData() = (merchantEditId != 0L)

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
                                updateAmountText(Util.getFormattedString(editMerchantData!!.amount))
                                updateDescText(editMerchantData!!.details.toString())

                                setDateAndTime(editMerchantData!!.dateInMilli)

                                loadPaymentData(editMerchantData!!.paymentId)
                                loadCategoryData(editMerchantData!!.categoryId)

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

    private fun loadCategoryData(id: Long) {
        viewModelScope.launch {
            getCategoryFromIdUseCase.getData(id)
                .collect {
                    setCategory(it)
                }
        }
    }

    fun onReceivedChange(isReceived: Boolean) {
        received.value = isReceived

        if (received.value)
            setCategory(categoryIncome)
        else
            setCategory(categoryExpense)
    }

    fun setDateAndTime(currentDateAndTime: Long) {
        currentTimeInMilli.value = currentDateAndTime
    }

    fun setMerchantData(data: MerchantNameAndDetails) {
        merchantError.value = ""
        merchant.value = data
    }

    fun setPaymentData(data: Payment) {
        paymentError.value = ""
        payment.value = data
    }

    fun setCategory(data: Category?) {
        categoryError.value = ""
        category.value = data

        if (received.value) {
            categoryIncome = data
            if (data != null) {
                if (data.type == 0 && categoryExpense == null) categoryExpense = data
            }
        } else {
            categoryExpense = data
            if (data != null) {
                if (data.type == 0 && categoryIncome == null) categoryIncome = data
            }
        }
    }

    fun addOrEditMerchantData(onSuccess: (Boolean, Long, Long) -> Unit) {
        if (enableButton.value) {
            enableButton.value = false
            if (merchant.value == null) {
                merchantError.value = ErrorMessage.SELECT_MERCHANT
                enableButton.value = true
            } else if (category.value == null) {
                categoryError.value = ErrorMessage.SELECT_CATEGORY
                enableButton.value = true
            } else if (amount.value.text.trim().isEmpty()) {
                amount.value.setError(ErrorMessage.AMOUNT_EMPTY)
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
                        categoryId = category.value!!.id,
                        amount = amount,
                        details = description.value.text.trim(),
                        dateInMilli = currentTimeInMilli.value,
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
                        categoryId = category.value!!.id,
                        amount = amount,
                        details = description.value.text.trim(),
                        dateInMilli = currentTimeInMilli.value,
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

    fun updateAmountText(text: String) = amount.value.updateText(text)
    fun updateDescText(text: String) = description.value.updateText(text)

}