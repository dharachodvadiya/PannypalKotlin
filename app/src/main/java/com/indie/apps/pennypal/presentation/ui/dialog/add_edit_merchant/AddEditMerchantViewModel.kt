package com.indie.apps.pennypal.presentation.ui.dialog.add_edit_merchant

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.cpp.data.repository.CountryRepository
import com.indie.apps.pennypal.data.database.entity.Merchant
import com.indie.apps.pennypal.data.module.ContactNumberAndCode
import com.indie.apps.pennypal.domain.usecase.AddMerchantUseCase
import com.indie.apps.pennypal.domain.usecase.UpdateMerchantUseCase
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.repository.MerchantRepository
import com.indie.apps.pennypal.repository.UserRepository
import com.indie.apps.pennypal.util.ErrorMessage
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditMerchantViewModel @Inject constructor(
    private val addMerchantUseCase: AddMerchantUseCase,
    private val updateMerchantUseCase: UpdateMerchantUseCase,
    private val userRepository: UserRepository,
    private val merchantRepository: MerchantRepository,
    private val countryRepository: CountryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val merchantEditId =
        savedStateHandle.get<String>(Util.PARAM_MERCHANT_ID)?.toLongOrNull() ?: -1

    val merchantName = MutableStateFlow(TextFieldState())
    val phoneNumber = MutableStateFlow(TextFieldState())
    val description = MutableStateFlow(TextFieldState())
    val countryDialCode = MutableStateFlow("")

    val enableButton = MutableStateFlow(true)

    private var editMerchant: Merchant? = null

    init {
        if (merchantEditId != -1L) {
            setEditId(merchantEditId)
        }
    }

    private fun setEditId(id: Long) {
        viewModelScope.launch {
            merchantRepository.getMerchantFromId(id)
                .collect {
                    editMerchant = it

                    updateNameText(it.name)
                    updatePhoneNoText(it.phoneNumber ?: "")
                    updateDescText(it.details ?: "")

                    if (!it.countryCode.isNullOrEmpty())
                        setCountryCode(it.countryCode)
                    //countryDialCode.value = it.countryCode
                }
        }
    }

    fun getIsEditable() = merchantEditId != -1L

    fun setCountryCode(code: String) {
        countryDialCode.value = code
    }

    fun setContactData(data: ContactNumberAndCode) {

        updateNameText(data.name)
        updatePhoneNoText(data.phoneNumber)

        //countryDialCode.value = data.dialCode ?: getDefaultCurrencyCode()
        setCountryCode(data.dialCode ?: getDefaultCurrencyCode())
    }

    fun addOrEditMerchant(onSuccess: (Merchant?, Boolean) -> Unit) {
        if (enableButton.value) {
            enableButton.value = false
            val isValidNum = Util.isValidPhoneNumber(
                countryCode = countryRepository.getCountryCodeFromDialCode(countryDialCode.value),
                phoneNumber = countryDialCode.value + phoneNumber.value.text
            )

            if (merchantName.value.text.trim().isEmpty()) {
                merchantName.value.setError(ErrorMessage.MERCHANT_NAME_EMPTY)
                enableButton.value = true
            } else if (phoneNumber.value.text.trim().isNotEmpty() && !isValidNum) {
                phoneNumber.value.setError(ErrorMessage.PHONE_NO_INVALID)
                enableButton.value = true
            } else {


                viewModelScope.launch {

                    if (merchantEditId != -1L) {
                        if (editMerchant != null) {
                            val merchant = editMerchant!!.copy(
                                id = merchantEditId,
                                name = merchantName.value.text.trim(),
                                phoneNumber = phoneNumber.value.text.trim(),
                                details = description.value.text.trim(),
                                countryCode = countryDialCode.value
                            )

                            updateMerchantUseCase
                                .updateData(merchant)
                                .collect {
                                    when (it) {
                                        is Resource.Loading -> {}
                                        is Resource.Success -> {
                                            onSuccess(merchant.copy(id = merchantEditId), true)
                                            enableButton.value = true
                                        }

                                        is Resource.Error -> {
                                            merchantName.value.setError(ErrorMessage.MERCHANT_EXIST)
                                            enableButton.value = true
                                        }
                                    }
                                }
                        }

                    } else {
                        val merchant = Merchant(
                            name = merchantName.value.text.trim(),
                            phoneNumber = phoneNumber.value.text.trim(),
                            details = description.value.text.trim(),
                            countryCode = countryDialCode.value,
                            //dateInMilli = System.currentTimeMillis()
                        )

                        addMerchantUseCase
                            .addMerchant(merchant)
                            .collect {
                                when (it) {
                                    is Resource.Loading -> {}
                                    is Resource.Success -> {
                                        onSuccess(merchant.copy(id = it.data!!), false)
                                        enableButton.value = true
                                    }

                                    is Resource.Error -> {
                                        merchantName.value.setError(ErrorMessage.MERCHANT_EXIST)
                                        enableButton.value = true
                                    }
                                }
                            }
                    }

                }
            }
        }

    }

    fun getDefaultCurrencyCode() =
        countryRepository.getDialCodeFromCountryCode(countryRepository.getDefaultCountryCode())

    fun updateNameText(text: String) = merchantName.value.updateText(text)
    fun updatePhoneNoText(text: String) = phoneNumber.value.updateText(text)
    fun updateDescText(text: String) = description.value.updateText(text)
}