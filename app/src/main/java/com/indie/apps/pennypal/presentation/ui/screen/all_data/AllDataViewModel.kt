package com.indie.apps.pennypal.presentation.ui.screen.all_data

import android.os.Bundle
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.indie.apps.pennypal.data.module.merchant_data.MerchantDataWithAllData
import com.indie.apps.pennypal.domain.usecase.DeleteMerchantDataUseCase
import com.indie.apps.pennypal.domain.usecase.LoadMerchantDataWithAllDataListUseCase
import com.indie.apps.pennypal.presentation.ui.state.PagingState
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.repository.AnalyticRepository
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
class AllDataViewModel @Inject constructor(
    searchMerchantDataWithAllDataListUseCase: LoadMerchantDataWithAllDataListUseCase,
    private val deleteMultipleMerchantDataUseCase: DeleteMerchantDataUseCase,
    private val analyticRepository: AnalyticRepository
) : ViewModel() {

    var scrollIndex = MutableStateFlow(0)
    var scrollOffset = MutableStateFlow(0)

    val searchTextState = MutableStateFlow(TextFieldState())
    private val trigger = MutableSharedFlow<Unit>(replay = 1)

    var selectedList = mutableStateListOf<Long>()
        private set

    var isDeletable = MutableStateFlow(false)

    val currentAnim = MutableStateFlow(AnimationType.NONE)
    val merchantAnimId = MutableStateFlow(-1L)

    private var previousData: PagingData<MerchantDataWithAllData>? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedData = trigger
        .flatMapLatest {
            searchMerchantDataWithAllDataListUseCase.loadData(searchTextState.value.text)
        }
        .map { item ->

            if (currentAnim.value != AnimationType.DELETE || previousData == null) {
                previousData = item
            }
            previousData!!
        }
        .cachedIn(viewModelScope)
    val pagingState = MutableStateFlow(PagingState<MerchantDataWithAllData>())


    init {

        searchData()
    }

    fun logEvent(name: String, params: Bundle? = null) {
        analyticRepository.logEvent(name, params)
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

    // Click

    fun onDeleteClick(onSuccess: () -> Unit) {
        onSuccess()
    }

    fun onAddClick(onSuccess: () -> Unit) {
        onSuccess()
    }

    fun onDeleteDialogClick(onSuccess: () -> Unit) {
        currentAnim.value = AnimationType.DELETE
        viewModelScope.launch {
            deleteMultipleMerchantDataUseCase
                .deleteDataList(selectedList)
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

    // Item Animation

    fun addDataSuccess(id: Long) {
        clearSelection()
        clearSearch()
        searchData()
        scrollIndex.value = 0
        scrollOffset.value = 0

        merchantAnimId.value = id
        //addAnimRun.value = true
        currentAnim.value = AnimationType.ADD

        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            onAnimationComplete(AnimationType.ADD)
        }
    }

    fun editDataSuccess(id: Long) {
        clearSelection()
        //clearSearch()
        searchData()

        merchantAnimId.value = id
        currentAnim.value = AnimationType.EDIT

        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            onAnimationComplete(AnimationType.EDIT)
        }
    }

    fun onDeleteFromEditScreenClick(id: Long, onSuccess: () -> Unit) {
        merchantAnimId.value = id
        currentAnim.value = AnimationType.DELETE
        viewModelScope.launch {
            deleteMultipleMerchantDataUseCase
                .deleteData(id)
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

    fun onAnimationComplete(animationType: AnimationType) {
        currentAnim.value = AnimationType.NONE
        merchantAnimId.value = -1L

        when (animationType) {
            AnimationType.DELETE -> {
                clearSelection()
                searchData()
            }

            else -> {}
        }
    }

}