package com.indie.apps.pannypal.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.domain.usecase.AddMerchantUseCase
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.state.TextFieldState
import com.indie.apps.pannypal.util.ErrorMessage
import com.indie.apps.pannypal.util.Resource
import com.mcode.ccp.data.utils.getLibCountries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMerchantViewModel @Inject constructor(
    private val addMerchantUseCase: AddMerchantUseCase
) : ViewModel() {

    val merchantName by mutableStateOf(TextFieldState())
    val phoneNumber by mutableStateOf(TextFieldState())
    val description by mutableStateOf(TextFieldState())
    var countryCode: String? by mutableStateOf(null)

    var enableButton by (mutableStateOf(true))
        private set

    fun addMerchant(onSuccess: (Merchant?) -> Unit) {
        if (enableButton) {
            enableButton = false
            val isValidNum = Util.isValidPhoneNumber(
                countryCode = getLibCountries.first { it.countryPhoneCode == countryCode }.countryCode,
                phoneNumber = countryCode + phoneNumber.text
            )

            if (merchantName.text.trim().isNullOrEmpty()) {
                merchantName.setError(ErrorMessage.MERCHANT_NAME_EMPTY)
                enableButton = true
            } else if (!phoneNumber.text.trim().isNullOrEmpty() && !isValidNum) {
                phoneNumber.setError(ErrorMessage.PHONE_NO_INVALID)
                enableButton = true
            } else {

                val merchant = Merchant(
                    name = merchantName.text.trim(),
                    phoneNumber = phoneNumber.text.trim(),
                    details = description.text.trim(),
                    countryCode = countryCode,
                    dateInMilli = System.currentTimeMillis()
                )
                viewModelScope.launch {
                    addMerchantUseCase
                        .addMerchant(merchant)
                        .collect {
                            when (it) {
                                is Resource.Loading -> {}
                                is Resource.Success -> {
                                    onSuccess(merchant.copy(id = it.data!!))
                                    enableButton = true
                                }

                                is Resource.Error -> {
                                    merchantName.setError(ErrorMessage.MERCHANT_EXIST)
                                    enableButton = true
                                }
                            }
                        }
                }
            }
        }

    }
}