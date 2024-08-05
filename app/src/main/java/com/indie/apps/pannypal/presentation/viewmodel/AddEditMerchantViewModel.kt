package com.indie.apps.pannypal.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.domain.usecase.AddMerchantUseCase
import com.indie.apps.pannypal.domain.usecase.GetMerchantFromIdUseCase
import com.indie.apps.pannypal.domain.usecase.UpdateMerchantUseCase
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.state.TextFieldState
import com.indie.apps.pannypal.util.ErrorMessage
import com.indie.apps.pannypal.util.Resource
import com.mcode.ccp.data.utils.getLibCountries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditMerchantViewModel @Inject constructor(
    private val addMerchantUseCase: AddMerchantUseCase,
    private val updateMerchantUseCase: UpdateMerchantUseCase,
    private val getMerchantFromIdUseCase: GetMerchantFromIdUseCase
) : ViewModel() {

    val merchantName by mutableStateOf(TextFieldState())
    val phoneNumber by mutableStateOf(TextFieldState())
    val description by mutableStateOf(TextFieldState())
    var countryCode: String? by mutableStateOf(null)

    var enableButton by (mutableStateOf(true))
        private set

    private var editId: Long? = null

    private var editMerchant : Merchant? = null

    fun setEditId(id: Long?) {
        editId = id
        if(editId != null)
        {
            viewModelScope.launch {
                getMerchantFromIdUseCase
                    .getData(editId!!)
                    .collect {
                        if(it is Resource.Success && it.data != null)
                        {
                            editMerchant = it.data

                            merchantName.text = it.data.name
                            phoneNumber.text = it.data.phoneNumber ?: ""
                            description.text = it.data.details ?: ""
                            if(!it.data.countryCode.isNullOrEmpty())
                                countryCode = it.data.countryCode
                        }
                    }
            }
        }


    }

    fun addOrEditMerchant(onSuccess: (Merchant?, Boolean) -> Unit) {
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


                viewModelScope.launch {

                    if (editId != null ) {
                        if(editMerchant != null)
                        {
                            val merchant = editMerchant!!.copy(
                                id = editId!!,
                                name = merchantName.text.trim(),
                                phoneNumber = phoneNumber.text.trim(),
                                details = description.text.trim(),
                                countryCode = countryCode,
                                dateInMilli = System.currentTimeMillis()
                            )

                            updateMerchantUseCase
                                .updateData(merchant)
                                .collect {
                                    when (it) {
                                        is Resource.Loading -> {}
                                        is Resource.Success -> {
                                            onSuccess(merchant.copy(id = editId!!), true)
                                            enableButton = true
                                        }

                                        is Resource.Error -> {
                                            merchantName.setError(ErrorMessage.MERCHANT_EXIST)
                                            enableButton = true
                                        }
                                    }
                                }
                        }

                    }else{
                        val merchant = Merchant(
                            name = merchantName.text.trim(),
                            phoneNumber = phoneNumber.text.trim(),
                            details = description.text.trim(),
                            countryCode = countryCode,
                            dateInMilli = System.currentTimeMillis()
                        )

                        addMerchantUseCase
                            .addMerchant(merchant)
                            .collect {
                                when (it) {
                                    is Resource.Loading -> {}
                                    is Resource.Success -> {
                                        onSuccess(merchant.copy(id = it.data!!), false)
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
}