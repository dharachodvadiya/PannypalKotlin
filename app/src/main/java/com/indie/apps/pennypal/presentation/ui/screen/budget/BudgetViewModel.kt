package com.indie.apps.pennypal.presentation.ui.screen.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.indie.apps.pennypal.data.database.enum.PeriodType
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIdList
import com.indie.apps.pennypal.domain.usecase.DeleteSingleBudgetDataUseCase
import com.indie.apps.pennypal.domain.usecase.GetBudgetsAndSpentWithCategoryIdListUseCase
import com.indie.apps.pennypal.domain.usecase.GetPastBudgetsAndSpentWithCategoryIdListFromPeriodType
import com.indie.apps.pennypal.domain.usecase.GetUpComingBudgetsAndSpentWithCategoryIdListFromPeriodType
import com.indie.apps.pennypal.util.Resource
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    getBudgetsAndSpentWithCategoryIdListUseCase: GetBudgetsAndSpentWithCategoryIdListUseCase,
    getPastBudgetsAndSpentWithCategoryIdListFromPeriodType: GetPastBudgetsAndSpentWithCategoryIdListFromPeriodType,
    getUpComingBudgetsAndSpentWithCategoryIdListFromPeriodType: GetUpComingBudgetsAndSpentWithCategoryIdListFromPeriodType,
    private val deleteSingleBudgetDataUseCase: DeleteSingleBudgetDataUseCase,
    //userRepository: UserRepository,
) : ViewModel() {

    /*val currency = userRepository.getCurrency()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), "$")*/

    private val calendar: Calendar = Calendar.getInstance()
    val currentPeriod = MutableStateFlow(PeriodType.MONTH)
    val currentMonthInMilli = MutableStateFlow(android.icu.util.Calendar.getInstance().timeInMillis)
    val currentYearInMilli = MutableStateFlow(android.icu.util.Calendar.getInstance().timeInMillis)

    val isExpandOneTimePastData = MutableStateFlow(false)
    val isExpandOneTimeUpcomingData = MutableStateFlow(false)
    val isExpandOneTimeActiveData = MutableStateFlow(true)


    var deleteAnimRun = MutableStateFlow(false)
    var addAnimRun = MutableStateFlow(false)

    val budgetAnimId = MutableStateFlow(-1L)

    private val triggerPastBudget = MutableSharedFlow<Unit>(replay = 1)
    private var previousDataPastBudget: PagingData<BudgetWithSpentAndCategoryIdList>? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedDataPastBudget = triggerPastBudget
        .flatMapLatest {
            getPastBudgetsAndSpentWithCategoryIdListFromPeriodType.loadData(
                year = calendar.get(Calendar.YEAR),
                month = calendar.get(Calendar.MONTH),
                periodType = currentPeriod.value.id
            )

        }
        .map { item ->

            if (!deleteAnimRun.value || previousDataPastBudget == null) {
                previousDataPastBudget = item
            }
            previousDataPastBudget!!
        }
        .cachedIn(viewModelScope)

    private var previousDataUpComingBudget: PagingData<BudgetWithSpentAndCategoryIdList>? = null
    private val triggerUpComingBudget = MutableSharedFlow<Unit>(replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedDataUpcomingBudget = triggerUpComingBudget
        .flatMapLatest {
            getUpComingBudgetsAndSpentWithCategoryIdListFromPeriodType.loadData(
                year = calendar.get(Calendar.YEAR),
                month = calendar.get(Calendar.MONTH),
                periodType = currentPeriod.value.id
            )

        }
        .map { item ->

            if (!deleteAnimRun.value || previousDataUpComingBudget == null) {
                previousDataUpComingBudget = item
            }
            previousDataUpComingBudget!!
        }
        .cachedIn(viewModelScope)


    private val trigger = MutableSharedFlow<Unit>(replay = 1)
    private var previousDataBudget: List<BudgetWithSpentAndCategoryIdList> = emptyList()

    @OptIn(ExperimentalCoroutinesApi::class)
    val budgetState = trigger
        .flatMapLatest {
            val calendar = getCurrentCalendar()
            getBudgetsAndSpentWithCategoryIdListUseCase.loadData(
                year = calendar.get(Calendar.YEAR),
                month = calendar.get(Calendar.MONTH),
                dataPeriod = currentPeriod.value
            )
        }
        .map { item ->

            if (!deleteAnimRun.value) {
                previousDataBudget = item
            }
            previousDataBudget
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    init {
        // setCurrentPeriod(PeriodType.MONTH)
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            trigger.tryEmit(Unit)

            if (currentPeriod.value == PeriodType.ONE_TIME) {
                loadOneTimePastData()
                loadOneTimeUpcomingData()
            }
        }
    }

    private fun loadOneTimePastData() {
        viewModelScope.launch {
            triggerPastBudget.tryEmit(Unit)
        }
    }

    private fun loadOneTimeUpcomingData() {
        viewModelScope.launch {
            triggerUpComingBudget.tryEmit(Unit)
        }
    }


    fun setCurrentPeriod(period: PeriodType) {
        if (currentPeriod.value == period) return
        currentPeriod.value = period
        loadData()
    }

    fun setCurrentPeriodWithTime(period: PeriodType, month: Int, year: Int) {
        currentPeriod.value = period
        when (currentPeriod.value) {
            PeriodType.MONTH -> setCurrentMonth(month, year)
            PeriodType.YEAR -> setCurrentYear(year)
            else -> {
                if (currentPeriod.value != period)
                    loadData()
            }
        }
    }

    private fun updateTime(amount: Int) {
        // if (isLoading.value) return
        val calendar = getCurrentCalendar().apply {
            when (currentPeriod.value) {
                PeriodType.MONTH -> add(android.icu.util.Calendar.MONTH, amount)
                PeriodType.YEAR -> add(android.icu.util.Calendar.YEAR, amount)
                else -> {}
            }
        }
        when (currentPeriod.value) {
            PeriodType.MONTH -> currentMonthInMilli.value = calendar.timeInMillis
            PeriodType.YEAR -> currentYearInMilli.value = calendar.timeInMillis
            else -> {}
        }
        loadData()
    }

    fun onPreviousClick() = updateTime(-1)
    fun onNextClick() = updateTime(1)

    fun formatDateForDisplay(period: PeriodType, timeInMillis1: Long): String {
        val calendar =
            android.icu.util.Calendar.getInstance().apply { timeInMillis = timeInMillis1 }
        return when (period) {
            PeriodType.MONTH -> {
                val dateFormat = SimpleDateFormat("MMM, yyyy", Locale.getDefault())
                dateFormat.format(calendar.time) // Example: "1 Jan, 2025"
            }

            PeriodType.YEAR -> {
                val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
                yearFormat.format(calendar.time) // Example: "2025"
            }

            else -> {
                ""
            }
        }
    }

    fun setCurrentMonth(month: Int, year: Int) {
        currentMonthInMilli.value = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.MONTH, month)
            set(java.util.Calendar.YEAR, year)
        }.timeInMillis
        loadData()
    }

    fun setCurrentYear(year: Int) {
        currentYearInMilli.value = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.YEAR, year)
        }.timeInMillis
        loadData()
    }

    fun onUpcomingExpandClick() {
        if (isExpandOneTimeUpcomingData.value)
            isExpandOneTimeUpcomingData.value = false
        else
            isExpandOneTimeUpcomingData.value = true
    }

    fun onPastExpandClick() {
        if (isExpandOneTimePastData.value)
            isExpandOneTimePastData.value = false
        else
            isExpandOneTimePastData.value = true
    }

    fun onActiveExpandClick() {
        if (isExpandOneTimeActiveData.value)
            isExpandOneTimeActiveData.value = false
        else
            isExpandOneTimeActiveData.value = true
    }

    private fun getCurrentCalendar(): android.icu.util.Calendar {
        return android.icu.util.Calendar.getInstance().apply {
            timeInMillis = when (currentPeriod.value) {
                PeriodType.YEAR -> currentYearInMilli.value
                PeriodType.MONTH -> currentMonthInMilli.value
                else -> Calendar.getInstance().timeInMillis
            }
        }
    }

    fun addBudgetSuccess(id: Long) {


        budgetAnimId.value = id
        addAnimRun.value = true

        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            addBudgetSuccessAnimStop()
            budgetAnimId.value = -1L
        }
    }

    fun addBudgetSuccessAnimStop() {
        if (addAnimRun.value)
            addAnimRun.value = false
    }

    fun onDeleteFromEditScreenClick(id: Long, onSuccess: () -> Unit) {
        budgetAnimId.value = id
        deleteAnimRun.value = true
        viewModelScope.launch {
            deleteSingleBudgetDataUseCase
                .deleteBudgetFromId(id)
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
            budgetAnimId.value = -1L
            deleteAnimRun.value = false
            loadData()
        }

    }

}