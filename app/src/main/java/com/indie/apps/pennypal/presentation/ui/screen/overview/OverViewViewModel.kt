package com.indie.apps.pennypal.presentation.ui.screen.overview

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.cpp.data.repository.CountryRepository
import com.indie.apps.pennypal.data.database.enum.PeriodType
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIdList
import com.indie.apps.pennypal.domain.usecase.GetBudgetFromPeriodUseCase
import com.indie.apps.pennypal.domain.usecase.GetCategoryWiseExpenseFromPreferencePeriodUseCase
import com.indie.apps.pennypal.domain.usecase.GetSpentAmountForPeriodAndCategoryUseCase
import com.indie.apps.pennypal.domain.usecase.GetTotalFromPreferencePeriodUseCase
import com.indie.apps.pennypal.domain.usecase.GetUserProfileUseCase
import com.indie.apps.pennypal.domain.usecase.SearchMerchantDataWithAllDataListUseCase
import com.indie.apps.pennypal.domain.usecase.SearchMerchantNameAndDetailListUseCase
import com.indie.apps.pennypal.repository.BillingRepository
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.ShowDataPeriod
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class OverViewViewModel @Inject constructor(
    userProfileUseCase: GetUserProfileUseCase,
    getTotalFromPreferencePeriodUseCase: GetTotalFromPreferencePeriodUseCase,
    searchMerchantDataWithAllDataListUseCase: SearchMerchantDataWithAllDataListUseCase,
    searchMerchantNameAndDetailListUseCase: SearchMerchantNameAndDetailListUseCase,
    getCategoryWiseExpenseFromPreferencePeriodUseCase: GetCategoryWiseExpenseFromPreferencePeriodUseCase,
    preferenceRepository: PreferenceRepository,
    getBudgetFromPeriodUseCase: GetBudgetFromPeriodUseCase,
    getSpentAmountForPeriodAndCategoryUseCase: GetSpentAmountForPeriodAndCategoryUseCase,
    private val billingRepository : BillingRepository,
    private val countryRepository: CountryRepository
) : ViewModel() {

    //val searchTextState by mutableStateOf(TextFieldState())

    val userData = userProfileUseCase.loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    val currentTotal = getTotalFromPreferencePeriodUseCase.loadData()
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
        .getLast3Data()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val recentMerchant = searchMerchantNameAndDetailListUseCase
        .getLast3Data()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val monthlyCategoryExpense = getCategoryWiseExpenseFromPreferencePeriodUseCase
        .loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    private val calendar: Calendar = Calendar.getInstance()

    @OptIn(ExperimentalCoroutinesApi::class)
    val budgetState = getBudgetFromPeriodUseCase.loadFromMonth(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH)
    ).flatMapConcat { budgets ->
        flow {
            val budgetWithSpentList = mutableListOf<BudgetWithSpentAndCategoryIdList>()

            // Process each budget one by one
            for (budget in budgets) {

                when (budget.periodType) {
                    PeriodType.MONTH.id -> {
                        val startCal : Calendar = Calendar.getInstance().apply { timeInMillis = budget.startDate }
                        getSpentAmountForPeriodAndCategoryUseCase.loadTotalAmountForMonth(
                            year = startCal.get(Calendar.YEAR),
                            month = startCal.get(Calendar.MONTH),
                            categoryIds = budget.category
                        ).collect { resource ->
                            when (resource) {
                                is Resource.Success -> {
                                    budgetWithSpentList.add(
                                        budget.copy(
                                            spentAmount = resource.data ?: 0.0
                                        )
                                    )
                                }

                                is Resource.Error -> {}
                                is Resource.Loading -> {}
                            }

                        }
                    }

                    PeriodType.YEAR.id -> {
                        val startCal : Calendar = Calendar.getInstance().apply { timeInMillis = budget.startDate }
                        getSpentAmountForPeriodAndCategoryUseCase.loadTotalAmountForYear(
                            year = startCal.get(Calendar.YEAR), categoryIds = budget.category
                        ).collect { resource ->
                            when (resource) {
                                is Resource.Success -> {
                                    budgetWithSpentList.add(
                                        budget.copy(
                                            spentAmount = resource.data ?: 0.0
                                        )
                                    )
                                }
                                is Resource.Error -> {}
                                is Resource.Loading -> {}
                            }
                        }
                    }

                    PeriodType.ONE_TIME.id -> getSpentAmountForPeriodAndCategoryUseCase.loadTotalAmountForBetweenDates(
                        startTime = budget.startDate,
                        endTime = budget.endDate ?: 0,
                        categoryIds = budget.category
                    ).collect { resource ->
                        when (resource) {
                            is Resource.Success -> {
                                budgetWithSpentList.add(
                                    budget.copy(
                                        spentAmount = resource.data ?: 0.0
                                    )
                                )
                            }is Resource.Error -> {}
                            is Resource.Loading -> {}
                        }
                    }
                }
            }
            emit(budgetWithSpentList)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    private val periodIndex = preferenceRepository.getInt(Util.PREF_BALANCE_VIEW, 1)
    val currentPeriod = MutableStateFlow(ShowDataPeriod.fromIndex(periodIndex))

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
        editAnimRun.value = true

        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            editAnimRun.value = false
        }
    }

    fun onSubscriptionChanged(isSubscribed: Boolean) {
        billingRepository.setSubscription(isSubscribed)
        this.isSubscribed.value = isSubscribed
    }

    fun getSymbolFromCurrencyCode(currencyCode: String): String {
        return countryRepository.getSymbolFromCurrencyCode(currencyCode)
    }
}