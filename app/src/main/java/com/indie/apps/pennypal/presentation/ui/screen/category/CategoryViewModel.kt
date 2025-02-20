package com.indie.apps.pennypal.presentation.ui.screen.category

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.indie.apps.pennypal.data.database.entity.Category
import com.indie.apps.pennypal.domain.usecase.DeleteMultipleCategoryUseCase
import com.indie.apps.pennypal.domain.usecase.SearchCategoryListUseCase
import com.indie.apps.pennypal.presentation.ui.state.PagingState
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    searchCategoryListUseCase: SearchCategoryListUseCase,
    private val deleteMultipleCategoryUseCase: DeleteMultipleCategoryUseCase
) : ViewModel() {

    var scrollIndex = MutableStateFlow(0)
    var scrollOffset = MutableStateFlow(0)

    val searchTextState = MutableStateFlow(TextFieldState())
    private val trigger = MutableSharedFlow<Unit>(replay = 1)

    var selectedList = mutableStateListOf<Long>()
        private set

    var isEditable = MutableStateFlow(false)
    var isDeletable = MutableStateFlow(false)

    var deleteAnimRun = MutableStateFlow(false)
    var addAnimRun = MutableStateFlow(false)
    var addDataAnimRun = MutableStateFlow(false)
    var editAnimRun = MutableStateFlow(false)
    private lateinit var previousData: PagingData<Category>

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedData = trigger
        .flatMapLatest {
            searchCategoryListUseCase.loadData(searchTextState.value.text)
        }
        .map { item ->

            if (!deleteAnimRun.value) {
                previousData = item
            }
            previousData
        }
        .cachedIn(viewModelScope)
    val pagingState = MutableStateFlow(PagingState<Category>())


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
            searchData()
        }
    }

    fun addCategorySuccess() {
        clearSelection()
        clearSearch()
        scrollIndex.value = 0
        scrollOffset.value = 0

        addAnimRun.value = true

        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            addCategorySuccessAnimStop()
        }
    }

    fun addCategorySuccessAnimStop() {
        if (addAnimRun.value)
            addAnimRun.value = false
    }

    fun editCategorySuccess() {
        clearSelection()
        //clearSearch()

        editAnimRun.value = true

        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            editAnimRun.value = false
        }
    }

    fun addMerchantDataSuccess() {

        clearSelection()
        clearSearch()
        scrollIndex.value = 0
        scrollOffset.value = 0

        addDataAnimRun.value = true

        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            addDataAnimRun.value = false
        }
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
        deleteAnimRun.value = true
        viewModelScope.launch {
            deleteMultipleCategoryUseCase
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

    fun updateSearchText(text: String) = searchTextState.value.updateText(text)

    fun getIsSelected() = selectedList.size != 0

}