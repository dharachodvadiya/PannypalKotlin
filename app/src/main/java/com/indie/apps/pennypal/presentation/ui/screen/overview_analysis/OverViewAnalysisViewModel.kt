package com.indie.apps.pennypal.presentation.ui.screen.overview_analysis

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.data.database.enum.AnalysisPeriod
import com.indie.apps.pennypal.domain.usecase.GetCategoryWiseExpenseFromPreferencePeriodUseCase
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.util.ShowDataPeriod
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.toAnalysisPeriod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class OverViewAnalysisViewModel @Inject constructor(
    getCategoryWiseExpenseFromPreferencePeriodUseCase: GetCategoryWiseExpenseFromPreferencePeriodUseCase,
    preferenceRepository: PreferenceRepository,
) : ViewModel() {

    val categoryExpense = getCategoryWiseExpenseFromPreferencePeriodUseCase
        .loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    private val periodIndex = preferenceRepository.getInt(Util.PREF_BALANCE_VIEW, 1)
    val currentPeriod = MutableStateFlow(
        ShowDataPeriod.fromIndex(periodIndex)?.toAnalysisPeriod() ?: AnalysisPeriod.MONTH
    )
    val currentMonthInMilli = MutableStateFlow(Calendar.getInstance().timeInMillis)
    val currentYearInMilli = MutableStateFlow(Calendar.getInstance().timeInMillis)

    fun setCurrentPeriod(period: AnalysisPeriod) {
        currentPeriod.value = period
    }

    fun onPreviousClick() {
        when (currentPeriod.value) {
            AnalysisPeriod.MONTH -> {
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = currentMonthInMilli.value
                    add(Calendar.MONTH, -1) // Move to previous month
                }
                currentMonthInMilli.value = calendar.timeInMillis
            }

            AnalysisPeriod.YEAR -> {
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = currentYearInMilli.value
                    add(Calendar.YEAR, -1) // Move to previous year
                }
                currentYearInMilli.value = calendar.timeInMillis
            }
        }
    }

    fun onNextClick() {
        when (currentPeriod.value) {
            AnalysisPeriod.MONTH -> {
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = currentMonthInMilli.value
                    add(Calendar.MONTH, 1) // Move to next month
                }
                currentMonthInMilli.value = calendar.timeInMillis
            }

            AnalysisPeriod.YEAR -> {
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = currentYearInMilli.value
                    add(Calendar.YEAR, 1) // Move to next year
                }
                currentYearInMilli.value = calendar.timeInMillis
            }
        }
    }

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
    }

    fun setCurrentYear(year: Int) {
        currentYearInMilli.value = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.YEAR, year)
        }.timeInMillis
    }

}