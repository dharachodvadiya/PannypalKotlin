package com.indie.apps.pennypal.presentation.ui.dialog.add_edit_merchant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.cpp.data.repository.CountryRepository
import com.indie.apps.pennypal.data.entity.Merchant
import com.indie.apps.pennypal.data.module.ContactNumberAndCode
import com.indie.apps.pennypal.domain.usecase.AddMerchantUseCase
import com.indie.apps.pennypal.domain.usecase.GetMerchantFromIdUseCase
import com.indie.apps.pennypal.domain.usecase.UpdateMerchantUseCase
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
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
    private val getMerchantFromIdUseCase: GetMerchantFromIdUseCase,
    private val countryRepository: CountryRepository
) : ViewModel() {

    val merchantName = MutableStateFlow(TextFieldState())
    val phoneNumber = MutableStateFlow(TextFieldState())
    val description = MutableStateFlow(TextFieldState())
    val countryDialCode = MutableStateFlow("")

    val enableButton = MutableStateFlow(true)

    private var editId: Long? = null

    private var editMerchant: Merchant? = null

    fun setEditId(id: Long?) {
        editId = id
        if (editId != null) {
            viewModelScope.launch {
                getMerchantFromIdUseCase
                    .getData(editId!!)
                    .collect {
                        editMerchant = it

                        merchantName.value.text = it.name
                        phoneNumber.value.text = it.phoneNumber ?: ""
                        description.value.text = it.details ?: ""
                        if (!it.countryCode.isNullOrEmpty())
                            countryDialCode.value = it.countryCode
                    }
            }
        }


    }

    fun setCountryCode(code: String) {
        countryDialCode.value = code
    }

    fun setContactNumber(contactNumber: String) {
        phoneNumber.value.text = contactNumber
    }

    fun setContactData(data : ContactNumberAndCode) {
        merchantName.value.text = data.name
        phoneNumber.value.text = data.phoneNumber
        countryDialCode.value = data.dialCode ?: getDefaultCurrencyCode()
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

                    if (editId != null) {
                        if (editMerchant != null) {
                            val merchant = editMerchant!!.copy(
                                id = editId!!,
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
                                            onSuccess(merchant.copy(id = editId!!), true)
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
                            dateInMilli = System.currentTimeMillis()
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
}