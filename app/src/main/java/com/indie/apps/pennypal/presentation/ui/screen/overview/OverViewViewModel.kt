package com.indie.apps.pennypal.presentation.ui.screen.overview

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.cpp.data.repository.CountryRepository
import com.indie.apps.pennypal.data.module.MonthlyTotal
import com.indie.apps.pennypal.domain.usecase.GetMerchantDataListWithMerchantNameAndDayTotalUseCase
import com.indie.apps.pennypal.domain.usecase.GetMonthlyTotalUseCase
import com.indie.apps.pennypal.domain.usecase.GetUserProfileUseCase
import com.indie.apps.pennypal.domain.usecase.GetYearlyTotalUseCase
import com.indie.apps.pennypal.domain.usecase.SearchMerchantDataWithAllDataListUseCase
import com.indie.apps.pennypal.domain.usecase.SearchMerchantNameAndDetailListUseCase
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverViewViewModel @Inject constructor(
    userProfileUseCase: GetUserProfileUseCase,
    getMonthlyTotalUseCase: GetMonthlyTotalUseCase,
    getYearlyTotalUseCase: GetYearlyTotalUseCase,
    searchMerchantDataWithAllDataListUseCase: SearchMerchantDataWithAllDataListUseCase,
    searchMerchantNameAndDetailListUseCase: SearchMerchantNameAndDetailListUseCase,
    getMerchantDataListWithMerchantNameAndDayTotalUseCase: GetMerchantDataListWithMerchantNameAndDayTotalUseCase,
    private val countryRepository: CountryRepository
) : ViewModel() {

    //val searchTextState by mutableStateOf(TextFieldState())

    /*val userData = userProfileUseCase.loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)*/


    val monthlyTotal = getMonthlyTotalUseCase.loadData(1)
        .flatMapConcat { monthlyTotals ->
            if (monthlyTotals.isEmpty()) {
                userProfileUseCase.loadData().map { user ->
                    listOf(MonthlyTotal("", 0.0, 0.0, user.currency))
                }
            } else {
                flowOf(monthlyTotals)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    var addDataAnimRun = MutableStateFlow(false)
        private set

    /*val pagedMerchantDataWithDay: Flow<PagingData<MerchantDataWithNameWithDayTotal>> =
        getMerchantDataListWithMerchantNameAndDayTotalUseCase
            .loadData()
            .cachedIn(viewModelScope)
    var merchantDataWithDayPagingState =
        MutableStateFlow(PagingState<MerchantDataWithNameWithDayTotal>())*/

    val recentTransaction = searchMerchantDataWithAllDataListUseCase
        .getLast3Data()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val recentMerchant = searchMerchantNameAndDetailListUseCase
        .getLast3Data()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())


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

    fun getSymbolFromCurrencyCode(currencyCode: String): String {
        return countryRepository.getSymbolFromCurrencyCode(currencyCode)
    }
}