package com.indie.apps.pennypal.presentation.ui.dialog.select_payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.domain.usecase.UpdateUserPaymentDataUseCase
import com.indie.apps.pennypal.repository.PaymentRepository
import com.indie.apps.pennypal.util.app_enum.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectPaymentViewModel @Inject constructor(
    paymentRepository: PaymentRepository,
    private val updateUserPaymentDataUseCase: UpdateUserPaymentDataUseCase,
) : ViewModel() {

    val paymentState = paymentRepository.getPaymentListWithMode()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    fun saveDefaultPayment(paymentId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            updateUserPaymentDataUseCase
                .updateData(paymentId)
                .collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            onSuccess()
                        }

                        is Resource.Error -> {
                        }
                    }
                }
        }
    }
}