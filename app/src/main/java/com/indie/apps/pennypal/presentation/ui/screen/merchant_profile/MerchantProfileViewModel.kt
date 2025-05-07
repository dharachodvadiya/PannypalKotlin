package com.indie.apps.pennypal.presentation.ui.screen.merchant_profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.data.database.db_entity.Merchant
import com.indie.apps.pennypal.repository.MerchantRepository
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.app_enum.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MerchantProfileViewModel @Inject constructor(
    private val merchantRepository: MerchantRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<Merchant>>(Resource.Loading())
    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }

    private fun getData() = viewModelScope.launch {
        merchantRepository.getMerchantFromId(
            savedStateHandle.get<String>(Util.PARAM_MERCHANT_ID)?.toLong() ?: 0
        )
            .collect {
                //  uiState = it
                _uiState.emit(Resource.Success(it))
            }
    }
}