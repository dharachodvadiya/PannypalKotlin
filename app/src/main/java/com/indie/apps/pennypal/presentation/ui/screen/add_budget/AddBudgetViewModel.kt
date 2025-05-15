package com.indie.apps.pennypal.presentation.ui.screen.add_budget

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.cpp.data.repository.CountryRepository
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.db_entity.BaseCurrency
import com.indie.apps.pennypal.data.database.db_entity.Category
import com.indie.apps.pennypal.data.database.db_entity.toCategoryAmount
import com.indie.apps.pennypal.data.module.budget.BudgetWithCategory
import com.indie.apps.pennypal.data.module.category.CategoryAmount
import com.indie.apps.pennypal.domain.usecase.AddBudgetUseCase
import com.indie.apps.pennypal.domain.usecase.UpdateBudgetUseCase
import com.indie.apps.pennypal.presentation.ui.component.UiText
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.repository.AnalyticRepository
import com.indie.apps.pennypal.repository.BaseCurrencyRepository
import com.indie.apps.pennypal.repository.BudgetRepository
import com.indie.apps.pennypal.repository.CategoryRepository
import com.indie.apps.pennypal.repository.UserRepository
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.app_enum.ErrorMessage
import com.indie.apps.pennypal.util.app_enum.PeriodType
import com.indie.apps.pennypal.util.app_enum.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AddBudgetViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val addBudgetUseCase: AddBudgetUseCase,
    private val updateBudgetUseCase: UpdateBudgetUseCase,
    private val budgetRepository: BudgetRepository,
    private val baseCurrencyRepository: BaseCurrencyRepository,
    private val userRepository: UserRepository,
    private val countryRepository: CountryRepository,
    private val analyticRepository: AnalyticRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val originalCurrencyInfo = MutableStateFlow<BaseCurrency?>(null)

    /* val currency = userRepository.getCurrency()
         .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), "$")*/

    val budgetEditId =
        savedStateHandle.get<String>(Util.PARAM_BUDGET_ID)?.toLongOrNull() ?: -1L
    private var editBudgetData: BudgetWithCategory? = null

    val currentPeriod = MutableStateFlow(PeriodType.MONTH.id)

    private var monthError = UiText.StringResource(R.string.empty)
    private var yearError = UiText.StringResource(R.string.empty)

    val periodErrorText = MutableStateFlow(UiText.StringResource(R.string.empty))
    val periodFromErrorText = MutableStateFlow(UiText.StringResource(R.string.empty))
    val periodToErrorText = MutableStateFlow(UiText.StringResource(R.string.empty))
    val categoryErrorText = MutableStateFlow(UiText.StringResource(R.string.empty))
    val categoryBudgetErrorText = MutableStateFlow(UiText.StringResource(R.string.empty))

    val currentMonthInMilli = MutableStateFlow(0L)
    val currentYearInMilli = MutableStateFlow(0L)
    val currentFromTimeInMilli = MutableStateFlow(0L)
    val currentToTimeInMilli = MutableStateFlow(0L)

    val amount = MutableStateFlow(TextFieldState())
    val remainingAmount = MutableStateFlow(0.0)

    val budgetTitle = MutableStateFlow(TextFieldState())


    private var categoryList = emptyList<Category>()

    val selectedCategoryList = MutableStateFlow<List<CategoryAmount>>(emptyList())

    val uiState = MutableStateFlow<Resource<Unit>>(Resource.Loading())

    private var isSetAsNewSelectedCategory = !isEditData()

    init {

        uiState.value = Resource.Loading()
        viewModelScope.launch {
            categoryRepository.getCategoryFromTypeList(
                type = -1
            ).collect { newCategories ->
                categoryList = newCategories

                if (budgetEditId == -1L) {
                    loadUserData()
                    val calendar = Calendar.getInstance()

                    setCurrentMonth(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR))
                    setCurrentYear(calendar.get(Calendar.YEAR))

                    selectedCategoryList.value = newCategories.map { it.toCategoryAmount() }

                    uiState.value = Resource.Success(Unit)

                } else {
                    setEditData()
                }
            }
        }
    }

    fun logEvent(name: String, params: Bundle? = null) {
        analyticRepository.logEvent(name, params)
    }

    private fun loadUserData() {
        //uiState.value = Resource.Loading()
        viewModelScope.launch {
            userRepository.getCurrencyCountryCode()
                .collect {
                    val baseCurrencyInfo =
                        baseCurrencyRepository.getBaseCurrencyFromCode(it)
                    setOriginalCurrencyInfo(baseCurrencyInfo)
                    //uiState.value = Resource.Success(Unit)

                }
        }
    }

    fun isEditData() = (budgetEditId != -1L)

    private fun setEditData() {
        viewModelScope.launch {
            try {
                budgetRepository.getBudgetWithCategoryFromId(budgetEditId).collect {
                    editBudgetData = it

                    currentPeriod.value = editBudgetData!!.periodType

                    when (currentPeriod.value) {
                        PeriodType.MONTH.id -> {
                            currentMonthInMilli.value = editBudgetData!!.startDate
                        }

                        PeriodType.YEAR.id -> {
                            currentYearInMilli.value = editBudgetData!!.startDate
                        }

                        PeriodType.ONE_TIME.id -> {
                            currentFromTimeInMilli.value = editBudgetData!!.startDate
                            currentToTimeInMilli.value = editBudgetData!!.endDate!!
                        }
                    }

                    /*setSelectedCategory(editBudgetData!!.category.map { cat -> cat.id })
                    editBudgetData!!.category.onEach { cat ->
                        setCategoryAmount(id = cat.id, amount = cat.amount.toString())
                    }*/

                    selectedCategoryList.value = editBudgetData!!.category


                    updateBudgetTitleText(editBudgetData!!.title)
                    updateAmountText(Util.getFormattedString(editBudgetData!!.amount))
                    updateRemainingAmount()

                    val originalCurrencyInfo =
                        baseCurrencyRepository.getBaseCurrencyFromId(editBudgetData!!.originalCurrencyId)

                    setOriginalCurrencyInfo(
                        data = originalCurrencyInfo
                    )


                    uiState.value = Resource.Success(Unit)

                }
            } catch (e: Exception) {
                uiState.value = Resource.Error("")
            }
        }
    }


    fun setCurrentPeriod(periodType: PeriodType) {
        currentPeriod.value = periodType.id
        when (currentPeriod.value) {
            PeriodType.MONTH.id -> {
                periodErrorText.value = monthError
            }

            PeriodType.YEAR.id -> {
                periodErrorText.value = yearError
            }

            else -> {
                periodErrorText.value = UiText.StringResource(R.string.empty)
            }
        }
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


    fun setSelectPeriodType(id: Int) {
        currentPeriod.value = id
    }

    fun setSelectedCategory(idList: List<Long>) {
        if (isSetAsNewSelectedCategory) {
            isSetAsNewSelectedCategory = false
            return
        }
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

    private fun setOriginalCurrencyInfo(data: BaseCurrency?) {
        originalCurrencyInfo.value = data
    }

    fun setCurrencyCountryCode(data: String) {
        val symbol = countryRepository.getCurrencySymbolFromCountryCode(data)
        setOriginalCurrencyInfo(BaseCurrency(currencyCountryCode = data, currencySymbol = symbol))
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
            categoryBudgetErrorText.value = UiText.StringResource(R.string.empty)
        }
    }

    fun saveData(onSuccess: (Boolean, Long, Int, Int) -> Unit) {
        clearAllError()
        if (budgetTitle.value.text.trim().isEmpty()) {
            budgetTitle.value.setError(ErrorMessage.BUDGET_TITLE_EMPTY)
        } else if (currentPeriod.value == PeriodType.ONE_TIME.id && currentFromTimeInMilli.value == 0L) {
            periodFromErrorText.value = ErrorMessage.SELECT_DATE
        } else if (currentPeriod.value == PeriodType.ONE_TIME.id && currentToTimeInMilli.value == 0L) {
            periodToErrorText.value = ErrorMessage.SELECT_DATE
        } else if (
            currentPeriod.value == PeriodType.ONE_TIME.id &&
            currentFromTimeInMilli.value >= currentToTimeInMilli.value
        ) {
            periodFromErrorText.value = UiText.StringResource(R.string.empty)
            periodToErrorText.value = ErrorMessage.INCORRECT_DATE
        } else if (currentPeriod.value == PeriodType.MONTH.id && currentMonthInMilli.value == 0L) {
            periodErrorText.value = ErrorMessage.SELECT_MONTH
        } else if (currentPeriod.value == PeriodType.YEAR.id && currentYearInMilli.value == 0L) {
            periodErrorText.value = ErrorMessage.SELECT_YEAR
        } else if (amount.value.text.trim().isEmpty() || amount.value.text.trim() == "0") {
            amount.value.setError(ErrorMessage.AMOUNT_EMPTY)
        } else if (selectedCategoryList.value.isEmpty()) {
            categoryErrorText.value = ErrorMessage.SELECT_CATEGORY
        } else if (remainingAmount.value < 0) {
            categoryBudgetErrorText.value = ErrorMessage.CATEGORY_LIMIT
        } else {
            viewModelScope.launch {

                val budgetWithCategory = creteBudgetWithCategory()

                val startDate = when (currentPeriod.value) {
                    PeriodType.ONE_TIME.id -> currentFromTimeInMilli.value
                    PeriodType.MONTH.id -> currentMonthInMilli.value
                    PeriodType.YEAR.id -> currentYearInMilli.value
                    else -> 0L
                }
                val startCalender = Calendar.getInstance().apply { timeInMillis = startDate }

                val month = startCalender.get(Calendar.MONTH)
                val year = startCalender.get(Calendar.YEAR)

                val baseCurrencyId = userRepository.getCurrencyId().first()
                val currentCurrencyId = budgetWithCategory.originalCurrencyId

                logEvent("set_budget_same_currency", Bundle().apply {
                    putBoolean("SameCurrency", baseCurrencyId == currentCurrencyId)
                })

                if (budgetEditId == -1L) {

                    addBudgetUseCase.addData(
                        budgetWithCategory = budgetWithCategory,
                        month = month,
                        year = year
                    ).collect {
                        when (it) {
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                onSuccess(false, it.data ?: -1, month, year)
                            }

                            is Resource.Error -> {
                                setEntryExistError()
                            }
                        }
                    }
                } else {
                    updateBudgetUseCase.updateData(
                        budgetWithCategory = budgetWithCategory,
                        month = startCalender.get(Calendar.MONTH),
                        year = startCalender.get(Calendar.YEAR)
                    ).collect {
                        when (it) {
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                onSuccess(true, editBudgetData!!.id, month, year)
                            }

                            is Resource.Error -> {
                                setEntryExistError()
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun creteBudgetWithCategory(): BudgetWithCategory {
        val baseCurrency =
            if (originalCurrencyInfo.value?.id != 0L) originalCurrencyInfo.value else {
                baseCurrencyRepository.getBaseCurrencyFromCode(
                    originalCurrencyInfo.value?.currencyCountryCode ?: "US"
                )
            }
        val originalCurrencyId = baseCurrency?.id
            ?: (originalCurrencyInfo.value?.let { baseCurrencyRepository.insert(it) } ?: -1)

        val startDate = when (currentPeriod.value) {
            PeriodType.ONE_TIME.id -> currentFromTimeInMilli.value
            PeriodType.MONTH.id -> currentMonthInMilli.value
            PeriodType.YEAR.id -> currentYearInMilli.value
            else -> 0L
        }

        val endDate = when (currentPeriod.value) {
            PeriodType.ONE_TIME.id -> currentToTimeInMilli.value
            else -> null
        }

        return if (budgetEditId == -1L) {
            BudgetWithCategory(
                title = budgetTitle.value.text,
                amount = amount.value.text.toDouble(),
                periodType = currentPeriod.value,
                category = selectedCategoryList.value,
                startDate = startDate,
                endDate = endDate,
                createdDate = Calendar.getInstance().timeInMillis,
                originalCurrencyId = originalCurrencyId,
                originalAmountSymbol = originalCurrencyInfo.value?.currencySymbol ?: ""
            )
        } else {
            editBudgetData!!.copy(
                title = budgetTitle.value.text,
                amount = amount.value.text.toDouble(),
                periodType = currentPeriod.value,
                category = selectedCategoryList.value,
                startDate = startDate,
                endDate = endDate,
                originalCurrencyId = originalCurrencyId,
                originalAmountSymbol = originalCurrencyInfo.value?.currencySymbol ?: ""
            )
        }

    }

    private fun setEntryExistError() {
        when (currentPeriod.value) {
            PeriodType.MONTH.id -> {
                monthError = ErrorMessage.BUDGET_EXIST_MONTH
                periodErrorText.value = monthError
            }

            PeriodType.YEAR.id -> {
                yearError = ErrorMessage.BUDGET_EXIST_YEAR
                periodErrorText.value = yearError
            }

            else -> {
                periodErrorText.value = UiText.StringResource(R.string.empty)
            }
        }
    }

    private fun clearAllError() {
        amount.value.setError(UiText.StringResource(R.string.empty))
        periodErrorText.value = UiText.StringResource(R.string.empty)
        periodFromErrorText.value = UiText.StringResource(R.string.empty)
        periodToErrorText.value = UiText.StringResource(R.string.empty)
        categoryErrorText.value = UiText.StringResource(R.string.empty)
        categoryBudgetErrorText.value = UiText.StringResource(R.string.empty)
        monthError = UiText.StringResource(R.string.empty)
        yearError = UiText.StringResource(R.string.empty)

    }

    fun updateAmountText(text: String) {
        amount.value.updateText(text)
        updateRemainingAmount()
    }

    fun updateBudgetTitleText(text: String) {
        budgetTitle.value.updateText(text)
    }

}