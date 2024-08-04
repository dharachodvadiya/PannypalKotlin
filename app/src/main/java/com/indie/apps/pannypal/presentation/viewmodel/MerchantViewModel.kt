package com.indie.apps.pannypal.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.domain.usecase.SearchMerchantListUseCase
import com.indie.apps.pannypal.presentation.ui.state.PagingState
import com.indie.apps.pannypal.presentation.ui.state.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MerchantViewModel @Inject constructor(
    searchMerchantListUseCase: SearchMerchantListUseCase
) : ViewModel() {

    val searchTextState by mutableStateOf(TextFieldState())
    private val trigger = MutableSharedFlow<Unit>(replay = 1)

    var selectedList = mutableStateListOf<Long>()
    private set

    val pagedData = trigger
        .flatMapLatest {
            searchMerchantListUseCase.loadData(searchTextState.text)
        }
        .cachedIn(viewModelScope)
    val pagingState by mutableStateOf(PagingState<Merchant>())

    init {

        searchData()
    }

    fun searchData() {
        viewModelScope.launch {
            trigger.emit(Unit)
        }
    }

    fun clearSearch() {
        if (!searchTextState.text.trim().isNullOrEmpty()) {
            searchTextState.text = ""
            searchData()
        }
    }

    fun setSelectItem(id: Long)
    {
        if(selectedList.contains(id))
            selectedList.remove(id)
        else
            selectedList.add(id)
    }

    fun onEditClick(onSuccess : (Long)->Unit)
    {
        val id = selectedList[0]
        clearSearch()
        selectedList.clear()
        onSuccess(id)
    }

    fun onDeleteClick(onSuccess : ()->Unit)
    {
        clearSearch()
        selectedList.clear()
        onSuccess()
    }

    fun onAddClick(onSuccess : ()->Unit)
    {
        clearSearch()
        onSuccess()
    }

    fun onNavigationUp(onSuccess : ()->Unit)
    {
        selectedList.clear()
        onSuccess()
    }
}