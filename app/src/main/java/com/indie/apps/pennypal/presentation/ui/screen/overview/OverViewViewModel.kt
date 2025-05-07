package com.indie.apps.pennypal.presentation.ui.screen.overview

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.data.module.merchant_data.MerchantDataWithAllData
import com.indie.apps.pennypal.domain.usecase.DeleteMerchantDataUseCase
import com.indie.apps.pennypal.domain.usecase.GetCategoryWiseExpenseUseCase
import com.indie.apps.pennypal.domain.usecase.GetTotalUseCase
import com.indie.apps.pennypal.domain.usecase.SearchMerchantDataWithAllDataListUseCase
import com.indie.apps.pennypal.domain.usecase.SearchMerchantNameAndDetailListUseCase
import com.indie.apps.pennypal.repository.BillingRepository
import com.indie.apps.pennypal.repository.BudgetRepository
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.repository.UserRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.ShowDataPeriod
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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
    private val deleteMultipleMerchantDataUseCase: DeleteMerchantDataUseCase,
    private val billingRepository: BillingRepository,
) : ViewModel() {

    /* val currency = userRepository.getCurrency()
         .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), "$")*/

    private val calendar: Calendar = Calendar.getInstance()
    private val periodIndex = preferenceRepository.getInt(Util.PREF_BALANCE_VIEW, 1)
    val currentPeriod = MutableStateFlow(ShowDataPeriod.fromIndex(periodIndex))

    //val searchTextState by mutableStateOf(TextFieldState())

    val userData = userRepository.getUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    // Current total, dependent on userData
    @OptIn(ExperimentalCoroutinesApi::class)
    val currentTotal = userData
        .flatMapLatest { user ->
            val currencyId = user?.currencyId ?: 1L // Default to 1 if null
            getTotalUseCase.loadDataAsFlow(
                year = calendar.get(Calendar.YEAR),
                month = calendar.get(Calendar.MONTH),
                dataPeriod = currentPeriod.value ?: ShowDataPeriod.THIS_MONTH,
                toCurrencyId = currencyId
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    var addDataAnimRun = MutableStateFlow(false)
        private set
    var editAnimRun = MutableStateFlow(false)

    val merchantDataAnimId = MutableStateFlow(-1L)

    var addMerchantAnimRun = MutableStateFlow(false)
        private set

    private val triggerRecentTransaction = MutableSharedFlow<Unit>(replay = 1)
    private var previousTransaction: List<MerchantDataWithAllData> = emptyList()

    @OptIn(ExperimentalCoroutinesApi::class)
    val recentTransaction = triggerRecentTransaction
        .flatMapLatest {
            searchMerchantDataWithAllDataListUseCase
                .getLast3DataFromPeriod(
                    year = calendar.get(Calendar.YEAR),
                    month = calendar.get(Calendar.MONTH),
                )
        }
        .map { item ->

            if (!deleteAnimRun.value) {
                previousTransaction = item
            }
            println("aaaa ${previousTransaction.size} .... ${item.size}")
            previousTransaction
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val recentMerchant = searchMerchantNameAndDetailListUseCase
        .getLast3Data()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val monthlyCategoryExpense = userData
        .flatMapLatest { user ->
            val currencyId = user?.currencyId ?: 1L // Default to 1 if null
            getCategoryWiseExpenseUseCase
                .loadDataAsFlow(
                    year = calendar.get(Calendar.YEAR),
                    month = calendar.get(Calendar.MONTH),
                    currentPeriod.value ?: ShowDataPeriod.THIS_MONTH,
                    toCurrencyId = currencyId
                )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())


    val budgetState = budgetRepository.getBudgetsAndSpentWithCategoryIdListFromMonth(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH),
        timeZoneOffsetInMilli = Util.TIME_ZONE_OFFSET_IN_MILLI
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())


    val isSubscribed = MutableStateFlow(billingRepository.getSubscription())

    //for delete transaction animation
    var deleteAnimRun = MutableStateFlow(false)

    init {
        loadTransactionData()
    }

    private fun loadTransactionData() {
        viewModelScope.launch {
            triggerRecentTransaction.emit(Unit)
        }
    }


    @SuppressLint("SuspiciousIndentation")
    fun addMerchantDataSuccess(id: Long) {
        merchantDataAnimId.value = id
        addDataAnimRun.value = true
        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            addMerchantDataSuccessAnimStop()
        }
    }

    fun addMerchantDataSuccessAnimStop() {
        if (addDataAnimRun.value) {
            addDataAnimRun.value = false
            merchantDataAnimId.value = -1L
        }
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

    fun editDataSuccess(id: Long) {
        viewModelScope.launch {
            delay(10L)
            merchantDataAnimId.value = id
            editAnimRun.value = true

            delay(Util.LIST_ITEM_ANIM_DELAY)
            editAnimRun.value = false
            merchantDataAnimId.value = -1L
        }
    }

    fun onSubscriptionChanged(isSubscribed: Boolean) {
        billingRepository.setSubscription(isSubscribed)
        this.isSubscribed.value = isSubscribed
    }

    fun onDeleteTransactionFromEditScreenClick(id: Long, onSuccess: () -> Unit) {
        merchantDataAnimId.value = id
        deleteAnimRun.value = true
        viewModelScope.launch {
            deleteMultipleMerchantDataUseCase
                .deleteData(id)
                .collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            onSuccess()
                            delay(Util.LIST_ITEM_ANIM_DELAY)
                            onDeleteAnimStop()

                        }

                        is Resource.Error -> {
                        }
                    }
                }
        }

    }

    fun onDeleteAnimStop() {
        if (deleteAnimRun.value) {
            merchantDataAnimId.value = -1L
            deleteAnimRun.value = false
            loadTransactionData()
        }

    }

    /*fun getSymbolFromCurrencyCode(currencyCode: String): String {
        return countryRepository.getSymbolFromCurrencyCode(currencyCode)
    }*/
}