package com.indie.apps.pannypal.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.domain.usecase.GetMerchantDataListFromMerchantIdUseCase
import com.indie.apps.pannypal.domain.usecase.GetMerchantFromIdUseCase
import com.indie.apps.pannypal.presentation.ui.state.PagingState
import com.indie.apps.pannypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MerchantDataViewModel @Inject constructor(
    private val getMerchantFromIdUseCase: GetMerchantFromIdUseCase,
    private val getMerchantDataListFromMerchantId: GetMerchantDataListFromMerchantIdUseCase
) : ViewModel() {

    var scrollIndex: Int by mutableStateOf(0)
    var scrollOffset: Int by mutableStateOf(0)

    var selectedList = mutableStateListOf<Long>()
        private set

    val merchantState = getMerchantFromIdUseCase
        .getData(1)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), Resource.Loading())

    val pagedData = getMerchantDataListFromMerchantId
        .loadData(1)
        .cachedIn(viewModelScope)

    val pagingState by mutableStateOf(PagingState<MerchantData>())
}