package com.indie.apps.pennypal.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.indie.apps.pennypal.data.entity.MerchantData
import com.indie.apps.pennypal.domain.usecase.DeleteMultipleMerchantDataUseCase
import com.indie.apps.pennypal.domain.usecase.GetMerchantDataListFromMerchantIdUseCase
import com.indie.apps.pennypal.domain.usecase.GetMerchantFromIdUseCase
import com.indie.apps.pennypal.presentation.ui.common.Util
import com.indie.apps.pennypal.presentation.ui.state.PagingState
import com.indie.apps.pennypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MerchantDataViewModel @Inject constructor(
    getMerchantFromIdUseCase: GetMerchantFromIdUseCase,
    getMerchantDataListFromMerchantId: GetMerchantDataListFromMerchantIdUseCase,
    private val deleteMultipleMerchantDataUseCase: DeleteMultipleMerchantDataUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val merchantId = savedStateHandle.get<String>(Util.PARAM_MERCHANT_ID)?.toLong() ?: 0

    var scrollIndex = MutableStateFlow(0)
    var scrollOffset = MutableStateFlow(0)

    var selectedList = mutableStateListOf<Long>()
        private set

    var isEditable = MutableStateFlow(false)
    var isDeletable = MutableStateFlow(false)

    val merchantState = getMerchantFromIdUseCase
        .getData(merchantId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    val pagedData = getMerchantDataListFromMerchantId
        .loadData(merchantId)
        .cachedIn(viewModelScope)

    val pagingState = MutableStateFlow(PagingState<MerchantData>())

    fun setScrollVal(scrollIndex: Int, scrollOffset: Int) {
        this.scrollIndex.value = scrollIndex
        this.scrollOffset.value = scrollOffset
    }

    fun onItemClick(id: Long) {
        if (!isEditable.value && !isDeletable.value) {
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
            isEditable.value = true
            isDeletable.value = true
        } else if (selectedCount > 1) {
            isEditable.value = false
            isDeletable.value = true
        } else {
            isEditable.value = false
            isDeletable.value = false
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