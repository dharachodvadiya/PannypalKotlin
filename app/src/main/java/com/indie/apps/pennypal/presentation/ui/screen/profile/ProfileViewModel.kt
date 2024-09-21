package com.indie.apps.pennypal.presentation.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.cpp.data.repository.CountryRepository
import com.indie.apps.pennypal.data.entity.User
import com.indie.apps.pennypal.domain.usecase.GetTotalFromMonthUseCase
import com.indie.apps.pennypal.domain.usecase.GetUserProfileUseCase
import com.indie.apps.pennypal.domain.usecase.UpdateUserDataUseCase
import com.indie.apps.pennypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    getMonthlyTotalUseCase: GetTotalFromMonthUseCase,
    private val userProfileUseCase: GetUserProfileUseCase,
    private val updateUserDataUseCase: UpdateUserDataUseCase,
    private val countryRepository: CountryRepository
) : ViewModel() {

    /* val uiState = userProfileUseCase.loadData()
         .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), Resource.Loading())*/

    /*var uiState: Resource<User> by mutableStateOf(Resource.Loading())
       private set*/

    //private val _uiState = MutableStateFlow<Resource<User>>(Resource.Loading())
    // val uiState = _uiState.asStateFlow()

    val currentMonthTotal = getMonthlyTotalUseCase.loadData(0)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    private var userData: User? = null
    val currUserData = MutableStateFlow<User?>(null)

    /*private val trigger = MutableSharedFlow<Unit>(replay = 1)



    val uiState = trigger.flatMapLatest { _ ->
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

                userData = it
                currUserData.value = it
                //  uiState = it
                //  _uiState.emit(it)


            }
    }

    fun setCountryCode(countryCode: String?) {
        countryCode?.let {
            currUserData.value = currUserData.value?.copy(currency = countryRepository.getCurrencyCodeFromCountryCode(it), currencyCountryCode = it)
        }
    }

    fun getIsSavable(): Boolean {
        if (userData != null) {
            return userData!!.currency.lowercase() != currUserData.value?.currency?.lowercase()
        }
        return false
    }

    fun saveUser(onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            updateUserDataUseCase
                .updateData(currUserData.value!!)
                .collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            onSuccess(true)
                        }

                        is Resource.Error -> {
                            onSuccess(false)
                        }
                    }
                }
        }
    }

    fun getSymbolFromCurrencyCode(currencyCode: String): String {
        return countryRepository.getSymbolFromCurrencyCode(currencyCode)
    }


}