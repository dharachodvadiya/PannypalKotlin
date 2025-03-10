package com.indie.apps.pennypal.presentation.ui.screen.all_data

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.indie.apps.pennypal.data.module.MerchantDataWithAllData
import com.indie.apps.pennypal.domain.usecase.DeleteMultipleMerchantDataUseCase
import com.indie.apps.pennypal.domain.usecase.SearchMerchantDataWithAllDataListUseCase
import com.indie.apps.pennypal.presentation.ui.state.PagingState
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.repository.UserRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllDataViewModel @Inject constructor(
    searchMerchantDataWithAllDataListUseCase: SearchMerchantDataWithAllDataListUseCase,
    private val deleteMultipleMerchantDataUseCase: DeleteMultipleMerchantDataUseCase,
    userRepository: UserRepository,
) : ViewModel() {

    val currency = userRepository.getCurrency()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), "US")

    var scrollIndex = MutableStateFlow(0)
    var scrollOffset = MutableStateFlow(0)

    val searchTextState = MutableStateFlow(TextFieldState())
    private val trigger = MutableSharedFlow<Unit>(replay = 1)

    var selectedList = mutableStateListOf<Long>()
        private set

    var isDeletable = MutableStateFlow(false)

    var deleteAnimRun = MutableStateFlow(false)
    var addAnimRun = MutableStateFlow(false)
    var editAnimRun = MutableStateFlow(false)
    private lateinit var previousData: PagingData<MerchantDataWithAllData>

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedData = trigger
        .flatMapLatest {
            searchMerchantDataWithAllDataListUseCase.loadData(searchTextState.value.text)
        }
        .map { item ->

            if (!deleteAnimRun.value) {
                previousData = item
            }
            previousData
        }
        .cachedIn(viewModelScope)
    val pagingState = MutableStateFlow(PagingState<MerchantDataWithAllData>())


    init {

        searchData()
    }

    fun searchData() {
        viewModelScope.launch {
            trigger.emit(Unit)
        }
    }

    private fun clearSearch() {
        if (searchTextState.value.text.trim().isNotEmpty()) {
            //searchTextState.value.text = ""
            updateSearchText("")
        }
    }

    fun addDataSuccess() {
        clearSelection()
        clearSearch()
        searchData()
        scrollIndex.value = 0
        scrollOffset.value = 0

        addAnimRun.value = true

        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            addDataSuccessAnimStop()
        }
    }

    fun addDataSuccessAnimStop() {
        if (addAnimRun.value)
            addAnimRun.value = false
    }

    fun editDataSuccess() {
        clearSelection()
        //clearSearch()
        searchData()

        editAnimRun.value = true

        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            editAnimRun.value = false
        }
    }

    fun onDeleteClick(onSuccess: () -> Unit) {
        onSuccess()
    }

    fun onAddClick(onSuccess: () -> Unit) {
        onSuccess()
    }

    fun onDeleteDialogClick(onSuccess: () -> Unit) {
        deleteAnimRun.value = true
        viewModelScope.launch {
            deleteMultipleMerchantDataUseCase
                .deleteData(selectedList)
                .collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            onSuccess()

                            delay(Util.LIST_ITEM_ANIM_DELAY)
                            onDeleteAnimStop()

                        }

                        is Resource.Error -> {
                        }
                    }
                }
        }

    }

    private fun onDeleteAnimStop() {
        if (deleteAnimRun.value) {
            deleteAnimRun.value = false
            clearSelection()
            searchData()
        }

    }

    fun onItemClick(id: Long, callBack: (Long) -> Unit) {
        if (!getIsSelected()) {
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
        if (selectedList.size > 0) {
            selectedList.clear()
            changeUpdateState()
        }
    }

    private fun changeUpdateState() {
        val selectedCount = selectedList.size
        isDeletable.value = selectedCount > 0
    }

    fun setScrollVal(scrollIndex: Int, scrollOffset: Int) {
        this.scrollIndex.value = scrollIndex
        this.scrollOffset.value = scrollOffset
    }

    fun updateSearchText(text: String) = searchTextState.value.updateText(text)

    fun getIsSelected() = selectedList.size != 0

    fun onBackClick(onSuccess: () -> Unit) {
        if (getIsSelected()) {
            clearSelection()
            searchData()
        } else if (searchTextState.value.text.trim().isNotEmpty()) {
            clearSearch()
            searchData()
        } else
            onSuccess()
    }

}