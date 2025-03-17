package com.indie.apps.pennypal.presentation.ui.screen.new_item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.cpp.data.repository.CountryRepository
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.entity.BaseCurrency
import com.indie.apps.pennypal.data.database.entity.Category
import com.indie.apps.pennypal.data.database.entity.MerchantData
import com.indie.apps.pennypal.data.database.entity.Payment
import com.indie.apps.pennypal.data.database.entity.toMerchantNameAndDetails
import com.indie.apps.pennypal.data.module.MerchantNameAndDetails
import com.indie.apps.pennypal.domain.usecase.AddMerchantDataUseCase
import com.indie.apps.pennypal.domain.usecase.GetMerchantDataFromIdUseCase
import com.indie.apps.pennypal.domain.usecase.GetPaymentFromIdUseCase
import com.indie.apps.pennypal.domain.usecase.UpdateMerchantDataUseCase
import com.indie.apps.pennypal.presentation.ui.component.UiText
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.repository.BaseCurrencyRepository
import com.indie.apps.pennypal.repository.CategoryRepository
import com.indie.apps.pennypal.repository.ExchangeRateRepository
import com.indie.apps.pennypal.repository.MerchantRepository
import com.indie.apps.pennypal.repository.UserRepository
import com.indie.apps.pennypal.util.AppError
import com.indie.apps.pennypal.util.ErrorMessage
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class NewItemViewModel @Inject constructor(
    private val addMerchantDataUseCase: AddMerchantDataUseCase,
    private val updateMerchantDataUseCase: UpdateMerchantDataUseCase,
    private val userRepository: UserRepository,
    private val getMerchantDataFromIdUseCase: GetMerchantDataFromIdUseCase,
    private val merchantRepository: MerchantRepository,
    private val getPaymentFromIdUseCase: GetPaymentFromIdUseCase,
    private val categoryRepository: CategoryRepository,
    private val countryRepository: CountryRepository,
    savedStateHandle: SavedStateHandle,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val baseCurrencyRepository: BaseCurrencyRepository
) : ViewModel() {

    /* val currency = userRepository.getCurrency()
         .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), "$")*/

    /* private val currencyCountryCode = MutableStateFlow("US")


     val currencySymbol = currencyCountryCode.map {
         countryRepository.getCurrencySymbolFromCountryCode(currencyCountryCode.value)
     }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), "$")*/

    //private val baseCurrencyCountry = MutableStateFlow<String>("US")
    //val baseCurrencySymbol = MutableStateFlow<String>("$")

    val baseCurrencyInfo = MutableStateFlow<BaseCurrency?>(null)
    val originalCurrencyInfo = MutableStateFlow<BaseCurrency?>(null)

    val isSameCurrency = MutableStateFlow(true)

    val finalAmount = MutableStateFlow<Double>(0.0)

    val merchantEditId =
        savedStateHandle.get<String>(Util.PARAM_EDIT_MERCHANT_DATA_ID)?.toLongOrNull() ?: 0
    private var editMerchantData: MerchantData? = null

    val merchant = MutableStateFlow<MerchantNameAndDetails?>(null)

    val payment = MutableStateFlow<Payment?>(null)

    val category = MutableStateFlow<Category?>(null)
    private var categoryIncome: Category? = null
    private var categoryExpense: Category? = null

    val received = MutableStateFlow(false)

    val enableButton = MutableStateFlow(true)

    val amount = MutableStateFlow(TextFieldState())
    val description = MutableStateFlow(TextFieldState())

    val rate = MutableStateFlow(TextFieldState())

    val currentTimeInMilli = MutableStateFlow(0L)

    var merchantError = MutableStateFlow(UiText.StringResource(R.string.empty))
    var paymentError = MutableStateFlow(UiText.StringResource(R.string.empty))
    var categoryError = MutableStateFlow(UiText.StringResource(R.string.empty))

    val uiState = MutableStateFlow<Resource<Unit>>(Resource.Loading())
    val rateState = MutableStateFlow<Resource<Unit>>(Resource.Loading())

    val categories = MutableStateFlow<List<Category>>(emptyList())

    init {

    }

    fun setInitialData() {
        if (merchantEditId == 0L) {
            loadUserData()
            loadCategoryData(1L)
            setDateAndTime(Calendar.getInstance().timeInMillis)
        } else
            setEditData()

        fetchLastUsedCategories()
    }

    private fun loadUserData() {
        uiState.value = Resource.Loading()
        viewModelScope.launch {
            userRepository.getUser()
                .collect {
                    loadPaymentData(it.paymentId)

                    val baseCurrencyInfo =
                        baseCurrencyRepository.getBaseCurrencyFromCode(it.currencyCountryCode)
                    setBaseCurrencyInfo(baseCurrencyInfo)
                    setOriginalCurrencyInfo(baseCurrencyInfo)
                    uiState.value = Resource.Success(Unit)

                }
        }
    }

    fun isEditData() = (merchantEditId != 0L)

    private fun setEditData() {
        viewModelScope.launch {
            getMerchantDataFromIdUseCase.getData(merchantEditId).collect { it ->
                when (it) {
                    is Resource.Loading -> uiState.value = Resource.Loading()
                    is Resource.Error -> uiState.value = Resource.Error("")

                    is Resource.Success -> {
                        if (it.data != null) {
                            editMerchantData = it.data
                            received.value =
                                editMerchantData!!.type == 1
                            updateAmountText(Util.getFormattedString(editMerchantData!!.originalAmount))
                            updateDescText(editMerchantData!!.details.toString())

                            setDateAndTime(editMerchantData!!.dateInMilli)

                            loadPaymentData(editMerchantData!!.paymentId)
                            loadCategoryData(editMerchantData!!.categoryId)

                            val baseCurrencyInfo =
                                baseCurrencyRepository.getBaseCurrencyFromId(editMerchantData!!.baseCurrencyId)

                            setBaseCurrencyInfo(
                                baseCurrencyInfo
                            )

                            val amount = editMerchantData!!.originalAmount
                            val finalAmount = editMerchantData!!.amount

                            val originalCurrencyInfo =
                                baseCurrencyRepository.getBaseCurrencyFromId(editMerchantData!!.originalCurrencyId)

                            setOriginalCurrencyInfo(
                                data = originalCurrencyInfo,
                                rate = (amount / finalAmount)
                            )

                            var merchantJob: Job? = null
                            merchantJob = launch {
                                editMerchantData!!.merchantId?.let { it1 ->
                                    merchantRepository.getMerchantFromId(it1)
                                        .collect {
                                            setMerchantData(it.toMerchantNameAndDetails())
                                            merchantJob?.cancel()
                                        }
                                }
                            }



                            uiState.value = Resource.Success(Unit)
                        } else {
                            uiState.value = Resource.Error("Not found")
                        }
                    }
                }
            }
        }
    }

    private fun loadPaymentData(id: Long) {
        viewModelScope.launch {
            getPaymentFromIdUseCase.getData(id)
                .collect {
                    setPaymentData(it)
                }
        }
    }

    private fun loadCategoryData(id: Long) {
        viewModelScope.launch {
            categoryRepository.getCategoryFromId(id)
                .collect {
                    setCategory(it)
                }
        }
    }

    fun onReceivedChange(isReceived: Boolean) {
        received.value = isReceived

        if (received.value)
            setCategory(categoryIncome)
        else
            setCategory(categoryExpense)
    }

    fun setDateAndTime(currentDateAndTime: Long) {
        currentTimeInMilli.value = currentDateAndTime
    }

    fun setMerchantData(data: MerchantNameAndDetails) {
        merchantError.value = UiText.StringResource(R.string.empty)
        merchant.value = data
    }

    private fun setOriginalCurrencyInfo(data: BaseCurrency?, rate: Double? = null) {
        //currencyCountryCode.value = data
        originalCurrencyInfo.value = data
        if (rate == null)
            loadRateAndFinalAmount()
        else {
            updateRateText(rate.toString())
            rateState.value = Resource.Success(Unit);
        }

        isSameCurrency.value =
            originalCurrencyInfo.value?.currencyCountryCode == baseCurrencyInfo.value?.currencyCountryCode

    }

    fun setCurrencyCountryCode(data: String) {
        val symbol = countryRepository.getCurrencySymbolFromCountryCode(data)
        setOriginalCurrencyInfo(BaseCurrency(currencyCountryCode = data, currencySymbol = symbol))
    }

    private fun setBaseCurrencyInfo(data: BaseCurrency?) {
        /* baseCurrencyCountry.value = data
         baseCurrencySymbol.value =
             countryRepository.getCurrencySymbolFromCountryCode(baseCurrencyCountry.value)*/

        baseCurrencyInfo.value = data
    }

    fun setPaymentData(data: Payment) {
        paymentError.value = UiText.StringResource(R.string.empty)
        payment.value = data
    }

    fun setCategory(data: Category?) {
        categoryError.value = UiText.StringResource(R.string.empty)
        category.value = data

        if (received.value) {
            categoryIncome = data
            if (data != null) {
                if (data.type == 0 && categoryExpense == null) categoryExpense = data
            }
        } else {
            categoryExpense = data
            if (data != null) {
                if (data.type == 0 && categoryIncome == null) categoryIncome = data
            }
        }
    }


    fun addOrEditMerchantData(onSuccess: (Boolean, Long, Long?) -> Unit) {
        if (!enableButton.value) return

        if (!isValidField()) return

        enableButton.value = false

        viewModelScope.launch {
            val merchantData = createMerchant()


            if (merchantEditId == 0L) {
                addMerchantDataUseCase.addData(merchantData).collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            enableButton.value = true
                            onSuccess(false, it.data ?: -1, merchantData.merchantId)
                        }

                        is Resource.Error -> {
                            enableButton.value = true
                        }
                    }
                }
            } else {
                updateMerchantDataUseCase.updateData(
                    merchantDataNew = merchantData
                ).collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            enableButton.value = true
                            onSuccess(true, merchantData.id, merchantData.merchantId)
                        }

                        is Resource.Error -> {
                            enableButton.value = true
                        }
                    }
                }
            }
        }
    }

    private fun isValidField(): Boolean {
        return when {
            category.value == null -> {
                categoryError.value = ErrorMessage.SELECT_CATEGORY
                false
            }

            amount.value.text.trim().isEmpty() -> {
                amount.value.setError(ErrorMessage.AMOUNT_EMPTY)
                false
            }

            payment.value == null -> {
                paymentError.value = ErrorMessage.SELECT_PAYMENT
                false
            }

            else -> {
                true
            }
        }

    }

    private suspend fun createMerchant(): MerchantData {


        val baseCurrency =
            if (originalCurrencyInfo.value?.id != 0L) originalCurrencyInfo.value else {
                baseCurrencyRepository.getBaseCurrencyFromCode(
                    originalCurrencyInfo.value?.currencyCountryCode ?: "US"
                )
            }
        val originalCurrencyId = baseCurrency?.id
            ?: (originalCurrencyInfo.value?.let { baseCurrencyRepository.insert(it) } ?: -1)
        val baseCurrencyId = baseCurrencyInfo.value?.id ?: 0

        val amount = amount.value.text.toDouble()
        return if (merchantEditId == 0L) {
            MerchantData(
                merchantId = merchant.value?.id,
                paymentId = payment.value!!.id,
                categoryId = category.value!!.id,
                amount = finalAmount.value,
                details = description.value.text.trim(),
                dateInMilli = currentTimeInMilli.value,
                type = if (received.value) 1 else -1,
                baseCurrencyId = baseCurrencyId,
                originalCurrencyId = originalCurrencyId,
                originalAmount = amount
            )
        } else {
            editMerchantData!!.copy(
                merchantId = merchant.value?.id,
                paymentId = payment.value!!.id,
                categoryId = category.value!!.id,
                amount = finalAmount.value,
                details = description.value.text.trim(),
                dateInMilli = currentTimeInMilli.value,
                type = if (received.value) 1 else -1,
                originalCurrencyId = originalCurrencyId,
                originalAmount = amount,
            )
        }
    }

    fun updateAmountText(text: String) {
        amount.value.updateText(text)
        updateFinalAmountWithRate()
    }

    fun updateDescText(text: String) = description.value.updateText(text)

    fun updateRateText(text: String) {
        rate.value.updateText(text)
        updateFinalAmountWithRate()
    }

    private fun updateFinalAmount(amt: Double) {
        finalAmount.value = amt
    }

    private fun fetchLastUsedCategories() {
        viewModelScope.launch {
            categories.value = categoryRepository.getRecentUsedCategoryList(5)
        }
    }

    fun loadRateAndFinalAmount() {
        viewModelScope.launch {
            rateState.value = Resource.Loading()
            val response =
                exchangeRateRepository.getConversionRate(
                    originalCurrencyInfo.value?.currencyCountryCode ?: "US",
                    baseCurrencyInfo.value?.currencyCountryCode ?: "US"
                )

            rateState.value = when (response) {
                is Resource.Error -> Resource.Error(
                    response.message ?: AppError.UnknownError.message
                )

                is Resource.Loading -> Resource.Loading()
                is Resource.Success -> {
                    updateRateText(response.data.toString())
                    Resource.Success(Unit)
                }
            }


        }
    }

    private fun updateFinalAmountWithRate() {
        val originalAmount =
            if (amount.value.text.trim().isEmpty()) 0.0 else amount.value.text.trim().toDouble()

        val rate = if (rate.value.text.trim().isEmpty()) 1.0 else rate.value.text.trim().toDouble()

        val amount = exchangeRateRepository.getAmountFromRate(originalAmount, rate)
        updateFinalAmount(amount)
    }

}