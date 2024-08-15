package com.indie.apps.pennypal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.indie.apps.pennypal.data.module.MerchantDataDailyTotal
import com.indie.apps.pennypal.data.module.MerchantDataWithName
import com.indie.apps.pennypal.domain.usecase.GetMerchantDataDailyTotalUseCase
import com.indie.apps.pennypal.domain.usecase.GetMerchantDataListWithMerchantNameUseCase
import com.indie.apps.pennypal.domain.usecase.GetUserProfileUseCase
import com.indie.apps.pennypal.presentation.ui.state.PagingState
import com.indie.apps.pennypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class OverViewViewModel @Inject constructor(
    userProfileUseCase: GetUserProfileUseCase,
    getMerchantDataListWithMerchantNameUseCase: GetMerchantDataListWithMerchantNameUseCase,
    getMerchantDataDailyTotalUseCase: GetMerchantDataDailyTotalUseCase
) : ViewModel() {

    //val searchTextState by mutableStateOf(TextFieldState())

    val uiState = userProfileUseCase.loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), Resource.Loading())

    val pagedMerchantData: Flow<PagingData<MerchantDataWithName>> =
        getMerchantDataListWithMerchantNameUseCase
            .loadData()
            .cachedIn(viewModelScope)
    var merchantDataWithNamePagingState = MutableStateFlow(PagingState<MerchantDataWithName>())

    val pagedMerchantDataDailyTotal: Flow<PagingData<MerchantDataDailyTotal>> =
        getMerchantDataDailyTotalUseCase
            .loadData()
            .cachedIn(viewModelScope)
    var merchantDataDailyTotalPagingState = MutableStateFlow(PagingState<MerchantDataDailyTotal>())

    //private val trigger = MutableSharedFlow<Unit>(replay = 1)

    /*fun searchData() {
        viewModelScope.launch {
            trigger.emit(Unit)
        }
    }*/
}