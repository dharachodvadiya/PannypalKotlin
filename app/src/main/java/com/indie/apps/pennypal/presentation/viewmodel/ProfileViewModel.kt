package com.indie.apps.pennypal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.data.entity.User
import com.indie.apps.pennypal.domain.usecase.GetUserProfileUseCase
import com.indie.apps.pennypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userProfileUseCase: GetUserProfileUseCase) :
    ViewModel() {

   /* val uiState = userProfileUseCase.loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), Resource.Loading())*/

     /*var uiState: Resource<User> by mutableStateOf(Resource.Loading())
        private set*/

    private val _uiState = MutableStateFlow<Resource<User>>(Resource.Loading())
    val uiState = _uiState.asStateFlow()

    //private val trigger = MutableSharedFlow<Unit>(replay = 1)



    /*val uiState = trigger.flatMapLatest { _ ->
        userProfileUseCase
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

     init {
         getData()
     }

     private fun getData() = viewModelScope.launch {
         userProfileUseCase
             .loadData()
             .collect {
               //  uiState = it
                 _uiState.emit(it)
         }
     }

}