package com.indie.apps.pannypal.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pannypal.data.entity.User
import com.indie.apps.pannypal.domain.usecase.getUserProfileUsecase
import com.indie.apps.pannypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userProfileUsecase: getUserProfileUsecase): ViewModel() {

    var userProfileUiState: Resource<User> by mutableStateOf(Resource.Loading())
        private set

    init {
        getData()
    }

    private fun getData() = viewModelScope.launch {
        Log.d("aaaaaaa", "getData call")
        userProfileUsecase.loadData().collect {
            userProfileUiState = it
        }
    }

}