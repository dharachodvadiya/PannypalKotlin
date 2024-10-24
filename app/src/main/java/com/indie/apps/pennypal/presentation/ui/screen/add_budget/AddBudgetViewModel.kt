package com.indie.apps.pennypal.presentation.ui.screen.add_budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.data.database.entity.Category
import com.indie.apps.pennypal.data.database.entity.toCategoryAmount
import com.indie.apps.pennypal.data.database.enum.BudgetPeriodType
import com.indie.apps.pennypal.data.module.category.CategoryAmount
import com.indie.apps.pennypal.domain.usecase.GetCategoryListUseCase
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.util.ErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AddBudgetViewModel @Inject constructor(
    getCategoryListUseCase: GetCategoryListUseCase
) : ViewModel() {

    val currentPeriod = MutableStateFlow(BudgetPeriodType.MONTH.id)
    val periodErrorText = MutableStateFlow("")
    val periodFromErrorText = MutableStateFlow("")
    val periodToErrorText = MutableStateFlow("")
    val categoryErrorText = MutableStateFlow("")
    val categoryBudgetErrorText = MutableStateFlow("")

    val currentMonthInMilli = MutableStateFlow(0L)
    val currentYearInMilli = MutableStateFlow(0L)
    val currentFromTimeInMilli = MutableStateFlow(0L)
    val currentToTimeInMilli = MutableStateFlow(0L)

    val amount = MutableStateFlow(TextFieldState())
    // val remainingAmount = MutableStateFlow(0.0)


    private var categoryList = emptyList<Category>()

    val selectedCategoryList = MutableStateFlow<List<CategoryAmount>>(emptyList())

    val remainingAmount = combine(
        amount,
        selectedCategoryList
    ) { total, categories ->
        ((total.text.toDoubleOrNull() ?: 0.0) - categories.sumOf { it.amount })
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 0.0)

    init {
        val calendar = Calendar.getInstance()

        setCurrentMonth(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR))
        setCurrentYear(calendar.get(Calendar.YEAR))

        viewModelScope.launch {
            getCategoryListUseCase
                .loadData(
                    type = -1
                ).collect { newCategories ->
                    categoryList = newCategories
                }
        }
    }


    fun setCurrentPeriod(budgetPeriodType: BudgetPeriodType) {
        currentPeriod.value = budgetPeriodType.id
    }

    fun setCurrentMonth(month: Int, year: Int) {
        currentMonthInMilli.value = Calendar.getInstance().apply {
            timeInMillis = 0
            set(Calendar.MONTH, month)
            set(Calendar.YEAR, year)
        }.timeInMillis
    }

    fun setCurrentYear(year: Int) {
        currentYearInMilli.value = Calendar.getInstance().apply {
            timeInMillis = 0
            set(Calendar.YEAR, year)
        }.timeInMillis
    }

    fun setCurrentFromTime(milli: Long) {
        val selectedCal = Calendar.getInstance().apply {
            timeInMillis = milli
        }
        currentFromTimeInMilli.value = Calendar.getInstance().apply {
            timeInMillis = 0
            set(Calendar.DAY_OF_MONTH, selectedCal.get(Calendar.DAY_OF_MONTH))
            set(Calendar.MONTH, selectedCal.get(Calendar.MONTH))
            set(Calendar.YEAR, selectedCal.get(Calendar.YEAR))
        }.timeInMillis
    }

    fun setCurrentToTime(milli: Long) {
        val selectedCal = Calendar.getInstance().apply {
            timeInMillis = milli
        }
        currentToTimeInMilli.value = Calendar.getInstance().apply {
            timeInMillis = 0
            set(Calendar.DAY_OF_MONTH, selectedCal.get(Calendar.DAY_OF_MONTH))
            set(Calendar.MONTH, selectedCal.get(Calendar.MONTH))
            set(Calendar.YEAR, selectedCal.get(Calendar.YEAR))
        }.timeInMillis
    }

    fun setSelectedCategory(idList: List<Long>) {
        val idSet = idList.toSet()

        val newList = mutableListOf<CategoryAmount>()

        selectedCategoryList.value.forEach { category ->
            if (idSet.contains(category.id)) {
                newList.add(category)
            }
        }

        idList.forEach { id ->
            if (newList.none { it.id == id }) {
                val newCategory = getCategoryById(id) // Implement this to retrieve CategoryAmount

                newCategory?.let { newList.add(it) }
            }
        }

        selectedCategoryList.value = newList
    }

    private fun getCategoryById(id: Long): CategoryAmount? {
        return categoryList.find { it.id == id }?.toCategoryAmount()
    }

    fun setCategoryAmount(id: Long, amount: String) {
        val newAmount = amount.toDoubleOrNull() ?: return

        val updatedList = selectedCategoryList.value.map { category ->
            if (category.id == id) {
                category.copy(amount = newAmount)
            } else {
                category
            }
        }
        selectedCategoryList.value = updatedList
    }

    /* private fun updateRemainingAmount(newTotal: Double? = null) {
         // Calculate the sum of amounts in selectedCategoryList
         val selectedSum = selectedCategoryList.value.sumOf { it.amount }

         // If newTotal is not null, use it; otherwise, get the current total
         val total = newTotal ?: amount.value.text.toDoubleOrNull() ?: 0.0

         // Calculate remaining amount
         remainingAmount.value = total - selectedSum
     }*/

    fun saveData(onSuccess: () -> Unit) {
        clearAllError()
        if (currentPeriod.value == BudgetPeriodType.ONE_TIME.id && currentFromTimeInMilli.value == 0L) {
            periodFromErrorText.value = ErrorMessage.SELECT_DATE
        } else if (currentPeriod.value == BudgetPeriodType.ONE_TIME.id && currentToTimeInMilli.value == 0L) {
            periodToErrorText.value = ErrorMessage.SELECT_DATE
        } else if (
            currentPeriod.value == BudgetPeriodType.ONE_TIME.id &&
            currentFromTimeInMilli.value >= currentToTimeInMilli.value
        ) {
            periodFromErrorText.value = ""
            periodToErrorText.value = ErrorMessage.INCORRECT
        } else if (currentPeriod.value == BudgetPeriodType.MONTH.id && currentMonthInMilli.value == 0L) {
            periodErrorText.value = ErrorMessage.SELECT_MONTH
        } else if (currentPeriod.value == BudgetPeriodType.YEAR.id && currentYearInMilli.value == 0L) {
            periodErrorText.value = ErrorMessage.SELECT_YEAR
        } else if (amount.value.text.trim().isEmpty() || amount.value.text.trim() == "0") {
            amount.value.setError(ErrorMessage.AMOUNT_EMPTY)
        } else if (selectedCategoryList.value.isEmpty()) {
            categoryErrorText.value = ErrorMessage.SELECT_CATEGORY
        } else {
            //add category amount sum error
        }
    }

    private fun clearAllError() {
        amount.value.setError("")
        periodErrorText.value = ""
        periodFromErrorText.value = ""
        periodToErrorText.value = ""
        categoryErrorText.value = ""
        categoryBudgetErrorText.value = ""
    }

}