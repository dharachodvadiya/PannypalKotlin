package com.indie.apps.pannypal.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.indie.apps.pannypal.data.entity.User
import com.indie.apps.pannypal.data.module.MerchantDataWithName
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails
import com.indie.apps.pannypal.domain.usecase.GetMerchantDataListWithMerchantNameUseCase
import com.indie.apps.pannypal.domain.usecase.GetUserProfileUseCase
import com.indie.apps.pannypal.presentation.ui.state.PagingState
import com.indie.apps.pannypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverViewViewModel @Inject constructor(
    private val userProfileUseCase: GetUserProfileUseCase,
    private val getMerchantDataListWithMerchantNameUseCase: GetMerchantDataListWithMerchantNameUseCase
) : ViewModel() {

    //val searchTextState by mutableStateOf(TextFieldState())

    val uiState = userProfileUseCase.loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), Resource.Loading())

    val pagedData: Flow<PagingData<MerchantDataWithName>> =
        getMerchantDataListWithMerchantNameUseCase
            .loadData()
            .cachedIn(viewModelScope)
    var pagingState by mutableStateOf(PagingState<MerchantDataWithName>())

    //private val trigger = MutableSharedFlow<Unit>(replay = 1)

    init {
        /*pagedData = trigger
            .flatMapLatest {
                getMerchantDataListWithMerchantNameUseCase.loadData()
            }
            .cachedIn(viewModelScope)*/

        //searchData()
    }

    /*fun searchData() {
        viewModelScope.launch {
            trigger.emit(Unit)
        }
    }*/
}