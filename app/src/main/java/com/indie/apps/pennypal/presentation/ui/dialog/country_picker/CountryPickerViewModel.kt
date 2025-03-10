package com.indie.apps.pennypal.presentation.ui.dialog.country_picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.cpp.data.model.Country
import com.indie.apps.cpp.data.repository.CountryRepository
import com.indie.apps.pennypal.domain.usecase.UpdateUserCurrencyDataUseCase
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryPickerViewModel @Inject constructor(
    private val countryRepository: CountryRepository,
    private val updateUserCurrencyDataUseCase: UpdateUserCurrencyDataUseCase
) :
    ViewModel() {


    val searchTextState = MutableStateFlow(TextFieldState())
    val uiState = MutableStateFlow<List<Country>>(emptyList())

    init {
        searchData(false)
    }


    fun searchData(isCurrency : Boolean)
    {
        if(isCurrency){
            val data = countryRepository.searchCountryForCurrency(searchTextState.value.text)
            //if (data.isNotEmpty())
                uiState.value = data
        }else {
            val data = countryRepository.searchCountryForDialCode(searchTextState.value.text)
            //if (data.isNotEmpty())
                uiState.value = data
        }
    }

    fun getFlagIdFromCountryCode(countryCode: String) : Int{
        return countryRepository.getFlagsFromCountryCode(countryCode)
    }

    fun saveDefaultCurrency(countryCode: String, onSuccess : ()-> Unit )
    {
        viewModelScope.launch {
            updateUserCurrencyDataUseCase
                .updateData(
                    currencyCountryCode =  countryCode)
                .collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            onSuccess()
                        }

                        is Resource.Error -> {
                        }
                    }
                }
        }
    }

    fun updateSearchText(text : String) = searchTextState.value.updateText(text)
}