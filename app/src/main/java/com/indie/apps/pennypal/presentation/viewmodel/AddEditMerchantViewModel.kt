package com.indie.apps.pennypal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.data.entity.Merchant
import com.indie.apps.pennypal.domain.usecase.AddMerchantUseCase
import com.indie.apps.pennypal.domain.usecase.GetMerchantFromIdUseCase
import com.indie.apps.pennypal.domain.usecase.UpdateMerchantUseCase
import com.indie.apps.pennypal.presentation.ui.common.Util
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.util.ErrorMessage
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.cpp.data.utils.getLibCountries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditMerchantViewModel @Inject constructor(
    private val addMerchantUseCase: AddMerchantUseCase,
    private val updateMerchantUseCase: UpdateMerchantUseCase,
    private val getMerchantFromIdUseCase: GetMerchantFromIdUseCase
) : ViewModel() {

    val merchantName = MutableStateFlow(TextFieldState())
    val phoneNumber = MutableStateFlow(TextFieldState())
    val description = MutableStateFlow(TextFieldState())
    val countryCode = MutableStateFlow("")

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
                            countryCode.value = it.countryCode
                    }
            }
        }


    }

    fun setCountryCode(code: String) {
        countryCode.value = code
    }

    fun addOrEditMerchant(onSuccess: (Merchant?, Boolean) -> Unit) {
        if (enableButton.value) {
            enableButton.value = false
            val isValidNum = Util.isValidPhoneNumber(
                countryCode = getLibCountries.first { it.countryPhoneCode == countryCode.value }.countryCode,
                phoneNumber = countryCode.value + phoneNumber.value.text
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
                                countryCode = countryCode.value
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
                            countryCode = countryCode.value,
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
}