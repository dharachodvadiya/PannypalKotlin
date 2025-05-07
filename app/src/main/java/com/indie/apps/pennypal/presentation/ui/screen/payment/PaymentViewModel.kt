package com.indie.apps.pennypal.presentation.ui.screen.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.domain.usecase.DeletePaymentUseCase
import com.indie.apps.pennypal.repository.PaymentRepository
import com.indie.apps.pennypal.repository.UserRepository
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.app_enum.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val deletePaymentUseCase: DeletePaymentUseCase,
    userRepository: UserRepository,
    paymentRepository: PaymentRepository,
) : ViewModel() {

    val userState = userRepository.getUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    val paymentWithModeState = paymentRepository.getPaymentListWithMode()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    var editAnimRun = MutableStateFlow(false)


    fun editPaymentSuccess() {
        editAnimRun.value = true

        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            editAnimRun.value = false
        }
    }

    fun onDeleteDialogClick(deleteId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            deletePaymentUseCase
                .deleteData(deleteId)
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