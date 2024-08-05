package com.indie.apps.pannypal.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.domain.usecase.DeleteMultipleMerchantUseCase
import com.indie.apps.pannypal.domain.usecase.GetMerchantDataListFromMerchantIdUseCase
import com.indie.apps.pannypal.domain.usecase.SearchMerchantListUseCase
import com.indie.apps.pannypal.presentation.ui.state.PagingState
import com.indie.apps.pannypal.presentation.ui.state.TextFieldState
import com.indie.apps.pannypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MerchantViewModel @Inject constructor(
    searchMerchantListUseCase: SearchMerchantListUseCase,
    private val deleteMultipleMerchantUseCase: DeleteMultipleMerchantUseCase
) : ViewModel() {

    var scrollIndex: Int by mutableStateOf(0)
    var scrollOffset: Int by mutableStateOf(0)

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

    fun setEditAddSuccess()
    {
        println("aaaaa set edit/Add success")
        selectedList.clear()
        clearSearch()
        scrollIndex = 0
        scrollOffset = 0
    }

    fun onEditClick(onSuccess : (Long)->Unit)
    {
        val id = selectedList[0]
        onSuccess(id)
    }

    fun onDeleteClick(onSuccess : ()->Unit)
    {
        onSuccess()
    }

    fun onAddClick(onSuccess : ()->Unit)
    {
        onSuccess()
    }

    fun onNavigationUp(onSuccess : ()->Unit)
    {
        selectedList.clear()
        onSuccess()
    }

    fun onDeleteDialogClick(onSuccess : ()->Unit)
    {
        viewModelScope.launch {
            deleteMultipleMerchantUseCase
                .deleteData(selectedList)
                .collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            selectedList.clear()
                            onSuccess()
                        }

                        is Resource.Error -> {
                        }
                    }
                }
        }

    }
}