package com.indie.apps.pennypal.presentation.ui.dialog.delete_payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.contacts.data.repo.ContactsRepository
import com.indie.apps.pennypal.data.module.PaymentWithIdName
import com.indie.apps.pennypal.domain.usecase.DeletePaymentUseCase
import com.indie.apps.pennypal.domain.usecase.GetPaymentListUseCase
import com.indie.apps.pennypal.domain.usecase.GetPaymentListWithModeUseCase
import com.indie.apps.pennypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class DeletePaymentViewModel@Inject constructor(
    private val deletePaymentUseCase: DeletePaymentUseCase,
    getPaymentListWithModeUseCase: GetPaymentListWithModeUseCase
) :ViewModel() {

    private var deleteId = 0L

    val paymentState = getPaymentListWithModeUseCase
        .loadData()
        .map {items ->
            items.filter { it.id != deleteId  }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val newPaymentId = MutableStateFlow(2L)

    fun setDeletePaymentData(paymentData : PaymentWithIdName)
    {
        deleteId = paymentData.id
    }

    fun selectPayment(id: Long)
    {
        newPaymentId.value = id
    }

    fun onDeleteDialogClick(deleteId: Long, onSuccess: (Long) -> Unit) {
        /*viewModelScope.launch {
            deletePaymentUseCase
                .deleteData(deleteId,newPaymentId.value )
                .collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            onSuccess(deleteId)
                        }
                        is Resource.Error -> {
                        }
                    }
                }
        }*/
    }


}