package com.indie.apps.pannypal.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.domain.usecase.DeleteMultipleMerchantUseCase
import com.indie.apps.pannypal.domain.usecase.SearchMerchantListUseCase
import com.indie.apps.pannypal.presentation.ui.state.PagingState
import com.indie.apps.pannypal.presentation.ui.state.TextFieldState
import com.indie.apps.pannypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MerchantViewModel @Inject constructor(
    searchMerchantListUseCase: SearchMerchantListUseCase,
    private val deleteMultipleMerchantUseCase: DeleteMultipleMerchantUseCase
) : ViewModel() {

    var scrollIndex = MutableStateFlow(0)
    var scrollOffset = MutableStateFlow(0)

    val searchTextState = MutableStateFlow(TextFieldState())
    private val trigger = MutableSharedFlow<Unit>(replay = 1)

    var selectedList = mutableStateListOf<Long>()
        private set

    var isEditable = MutableStateFlow(false)
    var isDeletable = MutableStateFlow(false)

    val pagedData = trigger
        .flatMapLatest {
            searchMerchantListUseCase.loadData(searchTextState.value.text)
        }
        .cachedIn(viewModelScope)
    val pagingState = MutableStateFlow(PagingState<Merchant>())

    init {

        searchData()
    }

    fun searchData() {
        viewModelScope.launch {
            trigger.emit(Unit)
        }
    }

    fun clearSearch() {
        if (!searchTextState.value.text.trim().isNullOrEmpty()) {
            searchTextState.value.text = ""
            searchData()
        }
    }

    fun setEditAddSuccess() {
        clearSelection()
        clearSearch()
        scrollIndex.value = 0
        scrollOffset.value = 0
    }

    fun onEditClick(onSuccess: (Long) -> Unit) {
        val id = selectedList[0]
        onSuccess(id)
    }

    fun onDeleteClick(onSuccess: () -> Unit) {
        onSuccess()
    }

    fun onAddClick(onSuccess: () -> Unit) {
        onSuccess()
    }

    fun onNavigationUp(onSuccess: () -> Unit) {
        clearSelection()
        onSuccess()
    }

    fun onDeleteDialogClick(onSuccess: () -> Unit) {
        viewModelScope.launch {
            deleteMultipleMerchantUseCase
                .deleteData(selectedList)
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

    fun onItemClick(id: Long, callBack: (Long) -> Unit) {
        if (!isEditable.value && !isDeletable.value) {
            callBack(id)
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

    private fun clearSelection() {
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

    fun setScrollVal(scrollIndex: Int, scrollOffset: Int) {
        this.scrollIndex.value = scrollIndex
        this.scrollOffset.value = scrollOffset
    }

}