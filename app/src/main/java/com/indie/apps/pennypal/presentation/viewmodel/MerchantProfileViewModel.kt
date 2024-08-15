package com.indie.apps.pennypal.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.data.entity.Merchant
import com.indie.apps.pennypal.domain.usecase.GetMerchantFromIdUseCase
import com.indie.apps.pennypal.presentation.ui.common.Util
import com.indie.apps.pennypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MerchantProfileViewModel @Inject constructor(
    private val getMerchantFromIdUseCase: GetMerchantFromIdUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<Merchant>>(Resource.Loading())
    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }

    private fun getData() = viewModelScope.launch {
        getMerchantFromIdUseCase
            .getData(savedStateHandle.get<String>(Util.PARAM_MERCHANT_ID)?.toLong() ?: 0)
            .collect {
                //  uiState = it
                _uiState.emit(Resource.Success(it))
            }
    }
}