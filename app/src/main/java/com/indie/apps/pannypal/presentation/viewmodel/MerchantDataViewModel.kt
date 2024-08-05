package com.indie.apps.pannypal.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.domain.usecase.DeleteMultipleMerchantDataUseCase
import com.indie.apps.pannypal.domain.usecase.GetMerchantDataListFromMerchantIdUseCase
import com.indie.apps.pannypal.domain.usecase.GetMerchantFromIdUseCase
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.state.PagingState
import com.indie.apps.pannypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MerchantDataViewModel @Inject constructor(
    private val getMerchantFromIdUseCase: GetMerchantFromIdUseCase,
    private val getMerchantDataListFromMerchantId: GetMerchantDataListFromMerchantIdUseCase,
    private val deleteMultipleMerchantDataUseCase: DeleteMultipleMerchantDataUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val merchantId = savedStateHandle?.get<String>(Util.PARAM_MERCHANT_ID)?.toLong() ?: 0

    var scrollIndex: Int by mutableStateOf(0)
    var scrollOffset: Int by mutableStateOf(0)

    var selectedList = mutableStateListOf<Long>()
        private set

    var isEditable by mutableStateOf(false)
    var isDeletable by mutableStateOf(false)

    val merchantState = getMerchantFromIdUseCase
        .getData(merchantId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    val pagedData = getMerchantDataListFromMerchantId
        .loadData(merchantId)
        .cachedIn(viewModelScope)

    val pagingState by mutableStateOf(PagingState<MerchantData>())


    fun onItemClick(id: Long) {
        if (!isEditable && !isDeletable) {
            //callBack(id)
        } else {
            setSelectItem(id)
        }
    }

    fun onItemLongClick(id: Long) {
        setSelectItem(id)
    }

    private fun setSelectItem(id: Long) {
        if (selectedList.contains(id))
            selectedList.remove(id)
        else
            selectedList.add(id)

        changeUpdateState()
    }

    fun clearSelection() {
        selectedList.clear()
        changeUpdateState()
    }

    private fun changeUpdateState() {
        val selectedCount = selectedList.size
        if (selectedCount == 1) {
            isEditable = true
            isDeletable = true
        } else if (selectedCount > 1) {
            isEditable = false
            isDeletable = true
        } else {
            isEditable = false
            isDeletable = false
        }
    }

    fun onDeleteDialogClick(onSuccess: () -> Unit) {
        viewModelScope.launch {
            deleteMultipleMerchantDataUseCase
                .deleteData(merchantId, selectedList)
                .collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            clearSelection()
                            onSuccess()
                        }

                        is Resource.Error -> {
                        }
                    }
                }
        }

    }

    fun onEditClick(onSuccess: (Long) -> Unit) {
        if (selectedList.size > 0) {
            onSuccess(selectedList[0])
            clearSelection()
        }

    }

}