package com.indie.apps.pennypal.presentation.ui.dialog.add_edit_payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.data.entity.Payment
import com.indie.apps.pennypal.data.entity.PaymentMode
import com.indie.apps.pennypal.domain.usecase.AddPaymentUseCase
import com.indie.apps.pennypal.domain.usecase.GetPaymentFromIdUseCase
import com.indie.apps.pennypal.domain.usecase.GetPaymentModeListUseCase
import com.indie.apps.pennypal.domain.usecase.UpdatePaymentUseCase
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.util.ErrorMessage
import com.indie.apps.pennypal.util.Resource
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
    getPaymentModeListUseCase: GetPaymentModeListUseCase,
) : ViewModel() {

    val paymentTypeState = MutableStateFlow(TextFieldState())
    val enableButton = MutableStateFlow(true)

    val selectedModeId = MutableStateFlow(1L)

    private var editId: Long? = null

    private var editPayment: Payment? = null

    val paymentModeState = getPaymentModeListUseCase.loadData()
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

    fun setEditId(id: Long?) {
        editId = id
        if (editId != null) {
            viewModelScope.launch {
                getPaymentFromIdUseCase
                    .getData(editId!!)
                    .collect {
                        editPayment = it

                        selectedModeId.value = editPayment!!.modeId
                        paymentTypeState.value.text = editPayment!!.name
                    }
            }
        }


    }

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
                    if (editId != null) {

                        if(editPayment != null) {

                            val payment = editPayment!!.copy(
                                id = editId!!,
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

}