package com.indie.apps.pennypal.presentation.ui.screen.overview_analysis

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.data.database.enum.AnalysisPeriod
import com.indie.apps.pennypal.data.database.enum.toShowDataPeriod
import com.indie.apps.pennypal.domain.usecase.GetCategoryWiseExpenseUseCase
import com.indie.apps.pennypal.domain.usecase.GetTotalUseCase
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.repository.UserRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.ShowDataPeriod
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.toAnalysisPeriod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class OverViewAnalysisViewModel @Inject constructor(
    getCategoryWiseExpenseUseCase: GetCategoryWiseExpenseUseCase,
    getTotalUseCase: GetTotalUseCase,
    preferenceRepository: PreferenceRepository,
    userRepository: UserRepository
) : ViewModel() {
    val currency = userRepository.getCurrency()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), "$")
    private val periodIndex = preferenceRepository.getInt(Util.PREF_BALANCE_VIEW, 1)

    val currentMonthInMilli = MutableStateFlow(Calendar.getInstance().timeInMillis)
    val currentYearInMilli = MutableStateFlow(Calendar.getInstance().timeInMillis)

    val currentPeriod = MutableStateFlow(
        ShowDataPeriod.fromIndex(periodIndex)?.toAnalysisPeriod() ?: AnalysisPeriod.MONTH
    )

    private val trigger = MutableSharedFlow<Unit>(replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentTotal = trigger
        .flatMapLatest {
            val calendar = getCurrentCalendar()
            getTotalUseCase.loadData(
                year = calendar.get(java.util.Calendar.YEAR),
                month = calendar.get(java.util.Calendar.MONTH),
                currentPeriod.value.toShowDataPeriod()
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), Resource.Loading())

    @OptIn(ExperimentalCoroutinesApi::class)
    val categoryExpense = trigger
        .flatMapLatest {
            val calendar = getCurrentCalendar()
            getCategoryWiseExpenseUseCase
                .loadData(
                    year = calendar.get(java.util.Calendar.YEAR),
                    month = calendar.get(java.util.Calendar.MONTH),
                    currentPeriod.value.toShowDataPeriod()
                )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), Resource.Loading())

    // ✅ Combined loading state
    val isLoading = combine(currentTotal, categoryExpense) { total, expense ->
        total is Resource.Loading || expense is Resource.Loading
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), true)

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            trigger.tryEmit(Unit)
        }
    }

    fun setCurrentPeriod(period: AnalysisPeriod) {
        if (isLoading.value || currentPeriod.value == period) return
        currentPeriod.value = period
        loadData()
    }

    private fun updateTime(amount: Int) {
        if (isLoading.value) return
        val calendar = getCurrentCalendar().apply {
            when (currentPeriod.value) {
                AnalysisPeriod.MONTH -> add(Calendar.MONTH, amount)
                AnalysisPeriod.YEAR -> add(Calendar.YEAR, amount)
            }
        }
        when (currentPeriod.value) {
            AnalysisPeriod.MONTH -> currentMonthInMilli.value = calendar.timeInMillis
            AnalysisPeriod.YEAR -> currentYearInMilli.value = calendar.timeInMillis
        }
        loadData()
    }

    fun onPreviousClick() = updateTime(-1)
    fun onNextClick() = updateTime(1)

    fun formatDateForDisplay(period: AnalysisPeriod, timeInMillis1: Long): String {
        val calendar = Calendar.getInstance().apply { timeInMillis = timeInMillis1 }
        return when (period) {
            AnalysisPeriod.MONTH -> {
                val dateFormat = SimpleDateFormat("MMM, yyyy", Locale.getDefault())
                dateFormat.format(calendar.time) // Example: "1 Jan, 2025"
            }

            AnalysisPeriod.YEAR -> {
                val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
                yearFormat.format(calendar.time) // Example: "2025"
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

    private fun getCurrentCalendar(): Calendar {
        return Calendar.getInstance().apply {
            timeInMillis = when (currentPeriod.value) {
                AnalysisPeriod.YEAR -> currentYearInMilli.value
                AnalysisPeriod.MONTH -> currentMonthInMilli.value
            }
        }
    }


}