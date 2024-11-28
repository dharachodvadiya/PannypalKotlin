package com.indie.apps.pennypal.presentation.ui.screen.add_budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.data.database.entity.Category
import com.indie.apps.pennypal.data.database.entity.toCategoryAmount
import com.indie.apps.pennypal.data.database.enum.BudgetPeriodType
import com.indie.apps.pennypal.data.module.budget.BudgetWithCategory
import com.indie.apps.pennypal.data.module.category.CategoryAmount
import com.indie.apps.pennypal.domain.usecase.AddBudgetUseCase
import com.indie.apps.pennypal.domain.usecase.GetCategoryListUseCase
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.util.ErrorMessage
import com.indie.apps.pennypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AddBudgetViewModel @Inject constructor(
    getCategoryListUseCase: GetCategoryListUseCase,
    private val addBudgetUseCase: AddBudgetUseCase,
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
    val remainingAmount = MutableStateFlow(0.0)

    val budgetTitle = MutableStateFlow(TextFieldState())


    private var categoryList = emptyList<Category>()

    val selectedCategoryList = MutableStateFlow<List<CategoryAmount>>(emptyList())

    /* val remainingAmount = combine(
         amount,
         selectedCategoryList
     ) { total, categories ->
         ((total.text.toDoubleOrNull() ?: 0.0) - categories.sumOf { it.amount })
     }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 0.0)
 */
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
            set(Calendar.MONTH, month)
            set(Calendar.YEAR, year)
        }.timeInMillis
    }

    fun setCurrentYear(year: Int) {
        currentYearInMilli.value = Calendar.getInstance().apply {
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
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
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
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
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
        val newAmount = amount.toDoubleOrNull() ?: 0.0

        val updatedList = selectedCategoryList.value.map { category ->
            if (category.id == id) {
                category.copy(amount = newAmount)
            } else {
                category
            }
        }
        selectedCategoryList.value = updatedList
        updateRemainingAmount()
    }

    private fun updateRemainingAmount() {
        // Calculate the sum of amounts in selectedCategoryList
        val selectedSum = selectedCategoryList.value.sumOf { it.amount }

        // If newTotal is not null, use it; otherwise, get the current total
        val total = amount.value.text.toDoubleOrNull() ?: 0.0

        // Calculate remaining amount
        remainingAmount.value = total - selectedSum
        if (remainingAmount.value < 0) {
            categoryBudgetErrorText.value = ErrorMessage.CATEGORY_LIMIT
        } else {
            categoryBudgetErrorText.value = ""
        }
    }

    fun saveData(onSuccess: () -> Unit) {
        clearAllError()
        if (budgetTitle.value.text.trim().isEmpty()) {
            budgetTitle.value.setError(ErrorMessage.BUDGET_TITLE_EMPTY)
        } else if (currentPeriod.value == BudgetPeriodType.ONE_TIME.id && currentFromTimeInMilli.value == 0L) {
            periodFromErrorText.value = ErrorMessage.SELECT_DATE
        } else if (currentPeriod.value == BudgetPeriodType.ONE_TIME.id && currentToTimeInMilli.value == 0L) {
            periodToErrorText.value = ErrorMessage.SELECT_DATE
        } else if (
            currentPeriod.value == BudgetPeriodType.ONE_TIME.id &&
            currentFromTimeInMilli.value >= currentToTimeInMilli.value
        ) {
            periodFromErrorText.value = ""
            periodToErrorText.value = ErrorMessage.INCORRECT_DATE
        } else if (currentPeriod.value == BudgetPeriodType.MONTH.id && currentMonthInMilli.value == 0L) {
            periodErrorText.value = ErrorMessage.SELECT_MONTH
        } else if (currentPeriod.value == BudgetPeriodType.YEAR.id && currentYearInMilli.value == 0L) {
            periodErrorText.value = ErrorMessage.SELECT_YEAR
        } else if (amount.value.text.trim().isEmpty() || amount.value.text.trim() == "0") {
            amount.value.setError(ErrorMessage.AMOUNT_EMPTY)
        } else if (selectedCategoryList.value.isEmpty()) {
            categoryErrorText.value = ErrorMessage.SELECT_CATEGORY
        } else if (remainingAmount.value < 0) {
            categoryBudgetErrorText.value = ErrorMessage.CATEGORY_LIMIT
        } else {
            viewModelScope.launch {
                val startDate = when (currentPeriod.value) {
                    BudgetPeriodType.ONE_TIME.id -> currentFromTimeInMilli.value
                    BudgetPeriodType.MONTH.id -> currentMonthInMilli.value
                    BudgetPeriodType.YEAR.id -> currentYearInMilli.value
                    else -> 0L
                }

                val endDate = when (currentPeriod.value) {
                    BudgetPeriodType.ONE_TIME.id -> currentToTimeInMilli.value
                    else -> null
                }

                val budgetWithCategory = BudgetWithCategory(
                    title = budgetTitle.value.text,
                    amount = amount.value.text.toDouble(),
                    periodType = currentPeriod.value,
                    category = selectedCategoryList.value,
                    startDate = startDate,
                    endDate = endDate,
                    createdDate = Calendar.getInstance().timeInMillis
                )
                addBudgetUseCase.addData(budgetWithCategory).collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            onSuccess()
                        }

                        is Resource.Error -> {}
                    }
                }
            }
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

    fun updateAmountText(text: String) {
        amount.value.updateText(text)
        updateRemainingAmount()
    }

    fun updateBudgetTitleText(text: String) {
        budgetTitle.value.updateText(text)
    }

}