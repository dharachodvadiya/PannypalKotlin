package com.indie.apps.pennypal.presentation.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.indie.apps.pennypal.data.module.MerchantDataWithNameWithDayTotal
import com.indie.apps.pennypal.domain.usecase.GetMerchantDataListWithMerchantNameAndDayTotalUseCase
import com.indie.apps.pennypal.domain.usecase.GetUserProfileUseCase
import com.indie.apps.pennypal.presentation.ui.state.PagingState
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverViewViewModel @Inject constructor(
    userProfileUseCase: GetUserProfileUseCase,
    getMerchantDataListWithMerchantNameAndDayTotalUseCase: GetMerchantDataListWithMerchantNameAndDayTotalUseCase
) : ViewModel() {

    //val searchTextState by mutableStateOf(TextFieldState())

    val uiState = userProfileUseCase.loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), Resource.Loading())

    /*val pagedMerchantData: Flow<PagingData<MerchantDataWithName>> =
        getMerchantDataListWithMerchantNameUseCase
            .loadData()
            .cachedIn(viewModelScope)
    var merchantDataWithNamePagingState = MutableStateFlow(PagingState<MerchantDataWithName>())*/

    /*val pagedMerchantDataDailyTotal: Flow<PagingData<MerchantDataDailyTotal>> =
        getMerchantDataDailyTotalUseCase
            .loadData()
            .cachedIn(viewModelScope)
    var merchantDataDailyTotalPagingState = MutableStateFlow(PagingState<MerchantDataDailyTotal>())*/

    var addDataAnimRun = MutableStateFlow(false)
        private set

    val pagedMerchantDataWithDay: Flow<PagingData<MerchantDataWithNameWithDayTotal>> =
        getMerchantDataListWithMerchantNameAndDayTotalUseCase
            .loadData()
            .cachedIn(viewModelScope)
    var merchantDataWithDayPagingState =
        MutableStateFlow(PagingState<MerchantDataWithNameWithDayTotal>())

    //private val trigger = MutableSharedFlow<Unit>(replay = 1)

    /*fun searchData() {
        viewModelScope.launch {
            trigger.emit(Unit)
        }
    }*/

    @SuppressLint("SuspiciousIndentation")
    fun addMerchantDataSuccess() {

        addDataAnimRun.value = true
        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            addMerchantDataSuccessAnimStop()
        }
    }


    fun addMerchantDataSuccessAnimStop() {
        if (addDataAnimRun.value)
            addDataAnimRun.value = false
    }
}