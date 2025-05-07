package com.indie.apps.pennypal.presentation.ui.screen.merchant

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.indie.apps.pennypal.data.database.db_entity.Merchant
import com.indie.apps.pennypal.domain.usecase.DeleteMultipleMerchantUseCase
import com.indie.apps.pennypal.domain.usecase.SearchMerchantListUseCase
import com.indie.apps.pennypal.presentation.ui.state.PagingState
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.app_enum.AnimationType
import com.indie.apps.pennypal.util.app_enum.Resource
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

    val currentAnim = MutableStateFlow(AnimationType.NONE)
    val currentAnimId = MutableStateFlow(-1L)
    private lateinit var previousData: PagingData<Merchant>

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedData = trigger
        .flatMapLatest {
            searchMerchantListUseCase.loadData(searchTextState.value.text)
        }
        .map { item ->

            if (currentAnim.value != AnimationType.DELETE) {
                previousData = item
            }
            previousData
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

    private fun clearSearch() {
        if (searchTextState.value.text.trim().isNotEmpty()) {
            //searchTextState.value.text = ""
            updateSearchText("")
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

    fun onDeleteDialogClick(onSuccess: () -> Unit) {
        currentAnim.value = AnimationType.DELETE
        viewModelScope.launch {
            deleteMultipleMerchantUseCase
                .deleteData(selectedList)
                .collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            onSuccess()

                            delay(Util.LIST_ITEM_ANIM_DELAY)
                            onAnimationComplete(AnimationType.DELETE)

                        }

                        is Resource.Error -> {
                        }
                    }
                }
        }

    }

    fun addMerchantSuccess(id: Long) {
        clearSelection()
        clearSearch()
        searchData()
        scrollIndex.value = 0
        scrollOffset.value = 0

        currentAnim.value = AnimationType.ADD
        currentAnimId.value = id

        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            onAnimationComplete(AnimationType.ADD)
        }
    }

    fun editMerchantSuccess(id: Long) {
        clearSelection()
        searchData()
        //clearSearch()

        currentAnim.value = AnimationType.EDIT
        currentAnimId.value = id

        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            onAnimationComplete(AnimationType.EDIT)
        }
    }

    fun addMerchantDataSuccess(id: Long) {

        clearSelection()
        clearSearch()
        searchData()
        scrollIndex.value = 0
        scrollOffset.value = 0

        currentAnim.value = AnimationType.EDIT
        currentAnimId.value = id

        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            onAnimationComplete(AnimationType.EDIT)
        }
    }

    fun onAnimationComplete(animationType: AnimationType) {
        currentAnim.value = AnimationType.NONE
        currentAnimId.value = -1L

        when (animationType) {
            AnimationType.DELETE -> {
                clearSelection()
                searchData()
            }

            else -> {}
        }
    }

}