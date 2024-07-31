package com.indie.apps.pannypal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.domain.usecase.addPaymentUsecase
import com.indie.apps.pannypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPaymentViewModel @Inject constructor(private val addPaymentUsecase: addPaymentUsecase) :
    ViewModel() {

    /*private val _paymentType = MutableStateFlow("")
    val paymentType = _paymentType.asStateFlow()

    fun setStudentName(name: String) {
        _paymentType.tryEmit(name)
    }*/

    fun addPayment(payment: Payment,onSuccess: ()-> Unit, onFail: () -> Unit) {

        viewModelScope.launch {
            addPaymentUsecase
                .addPayment(payment)
                .collect { it ->
                    when(it)
                    {
                        is Resource.Loading -> {}
                        is Resource.Success -> onSuccess()
                        is Resource.Error -> onFail()
                    }
                }
        }
    }

}