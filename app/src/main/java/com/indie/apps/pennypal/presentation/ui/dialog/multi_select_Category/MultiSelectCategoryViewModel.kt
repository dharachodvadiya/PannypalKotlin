package com.indie.apps.pennypal.presentation.ui.dialog.multi_select_Category

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.domain.usecase.SearchCategoryListUseCase
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MultiSelectCategoryViewModel @Inject constructor(
    searchCategoryListUseCase: SearchCategoryListUseCase
) : ViewModel() {

    val searchTextState = MutableStateFlow(TextFieldState())
    val selectAllState = MutableStateFlow(false)
    private val trigger = MutableSharedFlow<Unit>(replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val categoryList = trigger
        .flatMapLatest {
            searchCategoryListUseCase
                .loadData(
                    searchQuery = searchTextState.value.text,
                    type = -1
                )
                .onEach {
                    if (it.isEmpty()) {
                        selectAllState.value = false
                    } else {
                        selectAllState.value = it.all { item -> selectedList.contains(item.id) }
                    }

                }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    var selectedList = mutableStateListOf<Long>()
        private set

    init {
        searchData()
    }


    fun searchData() {
        viewModelScope.launch {
            trigger.emit(Unit)
        }
    }

    fun setPreSelectedItem(ids: List<Long>) {
        selectedList.addAll(ids)
    }

    fun selectItem(id: Long) {
        if (selectedList.contains(id))
            selectedList.remove(id)
        else
            selectedList.add(id)

        if (categoryList.value.isEmpty()) {
            selectAllState.value = false
        } else {
            selectAllState.value = categoryList.value.all { item -> selectedList.contains(item.id) }
        }


    }

    fun selectAllClick(isSelectAll: Boolean) {
        if (isSelectAll) {
            categoryList.value.forEach() {
                if (!selectedList.contains(it.id))
                    selectedList.add(it.id)
            }
        } else {
            selectedList.clear()
        }
        selectAllState.value = isSelectAll
    }

    fun updateSearchText(text: String) = searchTextState.value.updateText(text)

}