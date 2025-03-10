package com.indie.apps.pennypal.presentation.ui.screen.overview

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.domain.usecase.GetCategoryWiseExpenseUseCase
import com.indie.apps.pennypal.domain.usecase.GetTotalUseCase
import com.indie.apps.pennypal.domain.usecase.SearchMerchantDataWithAllDataListUseCase
import com.indie.apps.pennypal.domain.usecase.SearchMerchantNameAndDetailListUseCase
import com.indie.apps.pennypal.repository.BillingRepository
import com.indie.apps.pennypal.repository.BudgetRepository
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.repository.UserRepository
import com.indie.apps.pennypal.util.ShowDataPeriod
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class OverViewViewModel @Inject constructor(
    userRepository: UserRepository,
    getTotalUseCase: GetTotalUseCase,
    searchMerchantDataWithAllDataListUseCase: SearchMerchantDataWithAllDataListUseCase,
    searchMerchantNameAndDetailListUseCase: SearchMerchantNameAndDetailListUseCase,
    getCategoryWiseExpenseUseCase: GetCategoryWiseExpenseUseCase,
    preferenceRepository: PreferenceRepository,
    budgetRepository: BudgetRepository,
    private val billingRepository: BillingRepository,
) : ViewModel() {

    val currency = userRepository.getCurrency()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), "US")

    private val calendar: Calendar = Calendar.getInstance()
    private val periodIndex = preferenceRepository.getInt(Util.PREF_BALANCE_VIEW, 1)
    val currentPeriod = MutableStateFlow(ShowDataPeriod.fromIndex(periodIndex))

    //val searchTextState by mutableStateOf(TextFieldState())

    val userData = userRepository.getUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    val currentTotal = getTotalUseCase.loadDataAsFlow(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH),
        currentPeriod.value ?: ShowDataPeriod.THIS_MONTH
    )
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    var addDataAnimRun = MutableStateFlow(false)
        private set

    var addMerchantAnimRun = MutableStateFlow(false)
        private set

    var editAnimRun = MutableStateFlow(false)

    /*val pagedMerchantDataWithDay: Flow<PagingData<MerchantDataWithNameWithDayTotal>> =
        getMerchantDataListWithMerchantNameAndDayTotalUseCase
            .loadData()
            .cachedIn(viewModelScope)
    var merchantDataWithDayPagingState =
        MutableStateFlow(PagingState<MerchantDataWithNameWithDayTotal>())*/

    val recentTransaction = searchMerchantDataWithAllDataListUseCase
        .getLast3DataFromPeriod(
            year = calendar.get(Calendar.YEAR),
            month = calendar.get(Calendar.MONTH),
        )
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val recentMerchant = searchMerchantNameAndDetailListUseCase
        .getLast3Data()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val monthlyCategoryExpense = getCategoryWiseExpenseUseCase
        .loadDataAsFlow(
            year = calendar.get(Calendar.YEAR),
            month = calendar.get(Calendar.MONTH),
            currentPeriod.value ?: ShowDataPeriod.THIS_MONTH
        )
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())


    val budgetState = budgetRepository.getBudgetsAndSpentWithCategoryIdListFromMonth(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH),
        timeZoneOffsetInMilli = Util.TIME_ZONE_OFFSET_IN_MILLI
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())


    val isSubscribed = MutableStateFlow(billingRepository.getSubscription())


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

    @SuppressLint("SuspiciousIndentation")
    fun addMerchantSuccess() {
        addMerchantAnimRun.value = true
        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            addMerchantSuccessAnimStop()
        }
    }

    fun addMerchantSuccessAnimStop() {
        if (addMerchantAnimRun.value)
            addMerchantAnimRun.value = false
    }

    fun editDataSuccess() {
        viewModelScope.launch {
            delay(10L)
            editAnimRun.value = true

            delay(Util.LIST_ITEM_ANIM_DELAY)
            editAnimRun.value = false
        }
    }

    fun onSubscriptionChanged(isSubscribed: Boolean) {
        billingRepository.setSubscription(isSubscribed)
        this.isSubscribed.value = isSubscribed
    }

    /*fun getSymbolFromCurrencyCode(currencyCode: String): String {
        return countryRepository.getSymbolFromCurrencyCode(currencyCode)
    }*/
}