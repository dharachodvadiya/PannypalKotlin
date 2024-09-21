package com.indie.apps.pennypal.presentation.ui.screen.overview

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.cpp.data.repository.CountryRepository
import com.indie.apps.pennypal.domain.usecase.GetCategoryWiseExpenseFromMonthUseCase
import com.indie.apps.pennypal.domain.usecase.GetTotalFromMonthUseCase
import com.indie.apps.pennypal.domain.usecase.GetUserProfileUseCase
import com.indie.apps.pennypal.domain.usecase.SearchMerchantDataWithAllDataListUseCase
import com.indie.apps.pennypal.domain.usecase.SearchMerchantNameAndDetailListUseCase
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverViewViewModel @Inject constructor(
    userProfileUseCase: GetUserProfileUseCase,
    getTotalFromMonthUseCase: GetTotalFromMonthUseCase,
    searchMerchantDataWithAllDataListUseCase: SearchMerchantDataWithAllDataListUseCase,
    searchMerchantNameAndDetailListUseCase: SearchMerchantNameAndDetailListUseCase,
    getCategoryWiseExpenseFromMonthUseCase: GetCategoryWiseExpenseFromMonthUseCase,
    private val countryRepository: CountryRepository
) : ViewModel() {

    //val searchTextState by mutableStateOf(TextFieldState())

    val userData = userProfileUseCase.loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    val currentMonthTotal = getTotalFromMonthUseCase.loadData(0)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    var addDataAnimRun = MutableStateFlow(false)
        private set

    var editAnimRun = MutableStateFlow(false)

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

    val monthlyCategoryExpense = getCategoryWiseExpenseFromMonthUseCase
        .loadData(0)
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

    fun editDataSuccess() {
        editAnimRun.value = true

        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            editAnimRun.value = false
        }
    }

    fun getSymbolFromCurrencyCode(currencyCode: String): String {
        return countryRepository.getSymbolFromCurrencyCode(currencyCode)
    }
}