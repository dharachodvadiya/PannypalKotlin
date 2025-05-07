package com.indie.apps.pennypal.presentation.ui.screen.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.indie.apps.pennypal.data.database.db_entity.Category
import com.indie.apps.pennypal.domain.usecase.DeleteCategoryUseCase
import com.indie.apps.pennypal.domain.usecase.SearchCategoryListUseCase
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
class CategoryViewModel @Inject constructor(
    searchCategoryListUseCase: SearchCategoryListUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase
) : ViewModel() {

    var scrollIndex = MutableStateFlow(0)
    var scrollOffset = MutableStateFlow(0)

    val searchTextState = MutableStateFlow(TextFieldState())
    private val trigger = MutableSharedFlow<Unit>(replay = 1)

    val currentAnim = MutableStateFlow(AnimationType.NONE)
    val currentAnimId = MutableStateFlow(-1L)
    private lateinit var previousData: PagingData<Category>

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedData = trigger
        .flatMapLatest {
            searchCategoryListUseCase.loadData(searchTextState.value.text)
        }
        .map { item ->

            if (currentAnim.value != AnimationType.DELETE) {
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
        }
    }


    fun onAddClick(onSuccess: () -> Unit) {
        onSuccess()
    }


    fun setScrollVal(scrollIndex: Int, scrollOffset: Int) {
        this.scrollIndex.value = scrollIndex
        this.scrollOffset.value = scrollOffset
    }

    fun updateSearchText(text: String) = searchTextState.value.updateText(text)

    fun onBackClick(onSuccess: () -> Unit) {
        if (searchTextState.value.text.trim().isNotEmpty()) {
            clearSearch()
            searchData()
        } else
            onSuccess()
    }

    fun onDeleteDialogClick(id: Long, onSuccess: () -> Unit) {
        currentAnim.value = AnimationType.DELETE
        currentAnimId.value = id
        viewModelScope.launch {
            deleteCategoryUseCase
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

    fun addCategorySuccess(id: Long) {
        //clearSelection()
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

    fun editCategorySuccess(id: Long) {
        //clearSelection()
        //clearSearch()
        searchData()

        currentAnim.value = AnimationType.EDIT
        currentAnimId.value = id

        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            onAnimationComplete(AnimationType.EDIT)
        }
    }

    fun addMerchantDataSuccess(id: Long) {

        //clearSelection()
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
                searchData()
            }

            else -> {}
        }
    }

}