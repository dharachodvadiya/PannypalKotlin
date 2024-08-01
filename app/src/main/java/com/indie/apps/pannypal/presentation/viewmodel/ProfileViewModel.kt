package com.indie.apps.pannypal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pannypal.domain.usecase.GetUserProfileUseCase
import com.indie.apps.pannypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userProfileUsecase: GetUserProfileUseCase) :
    ViewModel() {

    val uiState = userProfileUsecase.loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), Resource.Loading())

    // var userProfileUiState: Resource<User> by mutableStateOf(Resource.Loading())
    //    private set

    /*private val _uiState = MutableStateFlow<Resource<User>>(Resource.Loading())
    val uiState= _uiState.asStateFlow()*/

    //private val trigger = MutableSharedFlow<Unit>(replay = 1)



    /*val uiState = trigger.flatMapLatest { _ ->
        userProfileUsecase
            .loadData()
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), Resource.Loading())

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            trigger.emit(Unit)
        }
    }*/

    /* init {
         getData()
     }

     private fun getData() = viewModelScope.launch {
         Log.d("aaaaaaa", "getData call")

         userProfileUsecase
             .loadData()
             .collect {
             //userProfileUiState = it

             _uiState.emit(it)
         }
     }*/

}