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
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.app_enum.AnimationType
import com.indie.apps.pennypal.util.app_enum.Resource
import com.indie.apps.pennypal.util.app_enum.ShowDataPeriod
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

    private val calendar: Calendar = Calendar.getInstance()
    private val periodIndex = preferenceRepository.getInt(Util.PREF_BALANCE_VIEW, 1)
    val currentPeriod = MutableStateFlow(ShowDataPeriod.fromIndex(periodIndex))

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

    val currentMerchantDataAnimId = MutableStateFlow(-1L)
    val currentMerchantDataAnim = MutableStateFlow(AnimationType.NONE)

    val currentMerchantAnim = MutableStateFlow(AnimationType.NONE)
    val currentMerchantAnimId = MutableStateFlow(-1L)

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

            if (currentMerchantDataAnim.value != AnimationType.DELETE) {
                previousTransaction = item
            }
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

    init {
        loadTransactionData()
    }

    private fun loadTransactionData() {
        viewModelScope.launch {
            triggerRecentTransaction.emit(Unit)
        }
    }

    fun onSubscriptionChanged(isSubscribed: Boolean) {
        billingRepository.setSubscription(isSubscribed)
        this.isSubscribed.value = isSubscribed
    }

    fun onDeleteTransactionFromEditScreenClick(id: Long, onSuccess: () -> Unit) {
        this.currentMerchantDataAnimId.value = id
        currentMerchantDataAnim.value = AnimationType.DELETE
        viewModelScope.launch {
            deleteMultipleMerchantDataUseCase
                .deleteData(id)
                .collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            onSuccess()
                            delay(Util.LIST_ITEM_ANIM_DELAY)
                            onMerchantDataAnimationComplete(AnimationType.DELETE)

                        }

                        is Resource.Error -> {
                        }
                    }
                }
        }

    }

    @SuppressLint("SuspiciousIndentation")
    fun addMerchantDataSuccess(id: Long) {
        this.currentMerchantDataAnimId.value = id
        currentMerchantDataAnim.value = AnimationType.ADD
        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            onMerchantDataAnimationComplete(AnimationType.ADD)
        }
    }

    fun editDataSuccess(id: Long) {
        currentMerchantDataAnimId.value = id
        currentMerchantDataAnim.value = AnimationType.EDIT
        viewModelScope.launch {
            delay(10L)


            delay(Util.LIST_ITEM_ANIM_DELAY)
            onMerchantDataAnimationComplete(AnimationType.EDIT)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun addMerchantSuccess(id: Long) {
        currentMerchantAnimId.value = id
        currentMerchantAnim.value = AnimationType.EDIT
        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            onMerchantAnimationComplete(AnimationType.ADD)
        }
    }



    fun onMerchantDataAnimationComplete(animationType: AnimationType) {
        currentMerchantDataAnim.value = AnimationType.NONE
        currentMerchantDataAnimId.value = -1L

        when (animationType) {
            AnimationType.DELETE -> {
                loadTransactionData()
            }

            else -> {}
        }
    }

    fun onMerchantAnimationComplete(animationType: AnimationType) {
        currentMerchantAnim.value = AnimationType.NONE
        currentMerchantAnimId.value = -1L

    }
}