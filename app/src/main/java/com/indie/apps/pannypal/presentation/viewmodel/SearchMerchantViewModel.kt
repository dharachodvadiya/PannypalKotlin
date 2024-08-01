package com.indie.apps.pannypal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pannypal.domain.usecase.GetMerchantNameAndDetailListUseCase
import com.indie.apps.pannypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchMerchantViewModel @Inject constructor(
    private val getMerchantListUseCase: GetMerchantNameAndDetailListUseCase
): ViewModel() {

   /* var newsPage = 1

    val uiState = getMerchantUseCase.loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), Resource.Loading())
*/
    var pageNo = 1
    private val trigger = MutableSharedFlow<Unit>(replay = 1)
    val uiState = trigger.flatMapLatest { _ ->
        getMerchantListUseCase
            .loadData(pageNo)
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), Resource.Loading())

}