package com.indie.apps.pennypal.presentation.ui.dialog.add_edit_payment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.data.database.entity.Payment
import com.indie.apps.pennypal.data.database.entity.PaymentMode
import com.indie.apps.pennypal.domain.usecase.AddPaymentUseCase
import com.indie.apps.pennypal.domain.usecase.GetPaymentFromIdUseCase
import com.indie.apps.pennypal.domain.usecase.UpdatePaymentUseCase
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.repository.PaymentModeRepository
import com.indie.apps.pennypal.util.ErrorMessage
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditPaymentViewModel @Inject constructor(
    private val addPaymentUseCase: AddPaymentUseCase,
    private val updatePaymentUseCase: UpdatePaymentUseCase,
    private val getPaymentFromIdUseCase: GetPaymentFromIdUseCase,
    paymentModeRepository: PaymentModeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val paymentEditId =
        savedStateHandle.get<String>(Util.PARAM_PAYMENT_ID)?.toLongOrNull() ?: -1

    val paymentTypeState = MutableStateFlow(TextFieldState())
    val enableButton = MutableStateFlow(true)

    val selectedModeId = MutableStateFlow(1L)

    private var editPayment: Payment? = null

    val paymentModeState = paymentModeRepository.getPaymentModeList()
        .map { paymentList ->

            val newList = emptyList<PaymentMode>().toMutableList()
            paymentList.forEachIndexed { index, item ->
                if (index > 1)
                    newList.add(item)
            }
            newList.add(paymentList[0])

            newList
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    init {
        if (paymentEditId != -1L) {
            setEditId(paymentEditId)
        }
    }

    private fun setEditId(id: Long) {
        viewModelScope.launch {
            getPaymentFromIdUseCase
                .getData(id)
                .collect {
                    editPayment = it

                    selectedModeId.value = editPayment!!.modeId
                    //paymentTypeState.value.text = editPayment!!.name
                    updatePaymentTypeText(editPayment!!.name)
                }
        }
    }

    fun getIsEditable() = paymentEditId != -1L

    fun onModeChange(id: Long) {
        selectedModeId.value = id
    }

    fun addEditPayment(onSuccess: (Payment?, Boolean) -> Unit) {

        if (enableButton.value) {
            enableButton.value = false
            if (paymentTypeState.value.text.trim().isEmpty()) {
                paymentTypeState.value.setError(ErrorMessage.AMOUNT_PAYMENT_TYPE)
                enableButton.value = true
            } else {
                viewModelScope.launch {
                    if (paymentEditId != -1L) {

                        if (editPayment != null) {

                            val payment = editPayment!!.copy(
                                id = paymentEditId,
                                name = paymentTypeState.value.text.trim(),
                                modeId = selectedModeId.value
                            )

                            updatePaymentUseCase
                                .updateData(payment)
                                .collect {
                                    when (it) {
                                        is Resource.Loading -> {}
                                        is Resource.Success -> {
                                            onSuccess(payment, true)
                                            enableButton.value = true
                                        }

                                        is Resource.Error -> {
                                            paymentTypeState.value.setError(ErrorMessage.PAYMENT_TYPE_EXIST)
                                            enableButton.value = true
                                        }
                                    }
                                }
                        }

                    } else {
                        val payment = Payment(
                            name = paymentTypeState.value.text.trim(),
                            modeId = selectedModeId.value
                        )
                        addPaymentUseCase
                            .addPayment(payment)
                            .collect {
                                when (it) {
                                    is Resource.Loading -> {}
                                    is Resource.Success -> {
                                        onSuccess(
                                            it.data?.let { it1 ->
                                                payment.copy(
                                                    id = it1
                                                )
                                            }, false
                                        )

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

    fun updatePaymentTypeText(text: String) = paymentTypeState.value.updateText(text)

}