package com.indie.apps.pannypal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails
import com.indie.apps.pannypal.domain.usecase.SearchMerchantNameAndDetailListUseCase
import com.indie.apps.pannypal.presentation.ui.state.PagingState
import com.indie.apps.pannypal.presentation.ui.state.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchMerchantViewModel @Inject constructor(
    private val searchMerchantNameAndDetailListUseCase: SearchMerchantNameAndDetailListUseCase
) : ViewModel() {

    val searchTextState = MutableStateFlow(TextFieldState())
    private val trigger = MutableSharedFlow<Unit>(replay = 1)

    val pagedData = trigger
        .flatMapLatest {
            searchMerchantNameAndDetailListUseCase.loadData(searchTextState.value.text)
        }
        .cachedIn(viewModelScope)
    val pagingState = MutableStateFlow(PagingState<MerchantNameAndDetails>())


    init {

        searchData()
    }

    fun searchData() {
        viewModelScope.launch {
            trigger.emit(Unit)
        }
    }

}