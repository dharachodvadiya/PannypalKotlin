package com.indie.apps.pennypal.presentation.ui.dialog.search_merchant

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.indie.apps.pennypal.data.module.MerchantNameAndDetails
import com.indie.apps.pennypal.domain.usecase.SearchMerchantNameAndDetailListUseCase
import com.indie.apps.pennypal.presentation.ui.state.PagingState
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.repository.AnalyticRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchMerchantViewModel @Inject constructor(
    private val searchMerchantNameAndDetailListUseCase: SearchMerchantNameAndDetailListUseCase,
    private val analyticRepository: AnalyticRepository
) : ViewModel() {

    val searchTextState = MutableStateFlow(TextFieldState())
    private val trigger = MutableSharedFlow<Unit>(replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
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

    fun updateSearchText(text: String) = searchTextState.value.updateText(text)

    fun logEvent(name: String, params: Bundle? = null) {
        analyticRepository.logEvent(name, params)
    }

}