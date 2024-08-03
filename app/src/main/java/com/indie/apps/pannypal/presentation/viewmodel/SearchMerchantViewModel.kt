package com.indie.apps.pannypal.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails
import com.indie.apps.pannypal.domain.usecase.SearchMerchantNameAndDetailListUseCase
import com.indie.apps.pannypal.presentation.ui.state.PagingState
import com.indie.apps.pannypal.presentation.ui.state.TextFieldState
import com.indie.apps.pannypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchMerchantViewModel @Inject constructor(
    private val searchMerchantNameAndDetailListUseCase: SearchMerchantNameAndDetailListUseCase
) : ViewModel() {

    val searchTextState by mutableStateOf(TextFieldState())
    private val trigger = MutableSharedFlow<Unit>(replay = 1)

    val pagedData = trigger
        .flatMapLatest {
            searchMerchantNameAndDetailListUseCase.loadData(searchTextState.text)
        }
        .cachedIn(viewModelScope)
    val pagingState by mutableStateOf(PagingState<MerchantNameAndDetails>())


    init {

        searchData()
    }

    fun searchData() {
        viewModelScope.launch {
            trigger.emit(Unit)
        }
    }

}