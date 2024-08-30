package com.indie.apps.pennypal.presentation.ui.dialog.cpp

import androidx.lifecycle.ViewModel
import com.indie.apps.cpp.data.repository.CountryRepository
import com.indie.apps.cpp.data.model.Country
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CppViewModel @Inject constructor(private val countryRepository: CountryRepository) :
    ViewModel() {


    val searchTextState = MutableStateFlow(TextFieldState())
    val uiState = MutableStateFlow<List<Country>>(emptyList())

    init {
        searchData()
    }


    fun searchData()
    {
        val data = countryRepository.searchCountryForDialCode(searchTextState.value.text)
        if(data.isNotEmpty())
            uiState.value = data
    }

    fun getFlagIdFromCountryCode(countryCode: String) : Int{
        return countryRepository.getFlagsFromCountryCode(countryCode)
    }
}