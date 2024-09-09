package com.indie.apps.pennypal.presentation.ui.screen.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.domain.usecase.DeletePaymentUseCase
import com.indie.apps.pennypal.domain.usecase.GetPaymentListWithModeUseCase
import com.indie.apps.pennypal.domain.usecase.GetUserProfileUseCase
import com.indie.apps.pennypal.domain.usecase.UpdateUserDataUseCase
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    //private val deletePaymentUseCase: DeletePaymentUseCase,
    private val updateUserDataUseCase: UpdateUserDataUseCase,
    userProfileUseCase: GetUserProfileUseCase,
    getPaymentListWithModeUseCase: GetPaymentListWithModeUseCase,
) : ViewModel() {

    val userState = userProfileUseCase
        .loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    val paymentWithModeState = getPaymentListWithModeUseCase.loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())


    val isInEditMode = MutableStateFlow(false)

    var editAnimRun = MutableStateFlow(false)

    fun setEditMode(isEditable: Boolean) {
        isInEditMode.value = isEditable
    }

    fun editPaymentSuccess() {
        editAnimRun.value = true

        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            editAnimRun.value = false
        }
    }

    fun saveEditedData(defaultPaymentId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (userState.value != null) {
                val user = userState.value!!.copy(paymentId = defaultPaymentId)
                updateUserDataUseCase
                    .updateData(user)
                    .collect {
                        when (it) {
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                onSuccess()
                                isInEditMode.value = false
                            }

                            is Resource.Error -> {
                            }
                        }
                    }
            }
        }

    }

    /*fun onDeleteDialogClick(deleteId: Long, onSuccess: () -> Unit) {
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
    }*/

}