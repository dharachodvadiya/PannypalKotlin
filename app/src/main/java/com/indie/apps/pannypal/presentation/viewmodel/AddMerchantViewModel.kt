package com.indie.apps.pannypal.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.domain.usecase.AddMerchantUseCase
import com.indie.apps.pannypal.util.ErrorMessage
import com.indie.apps.pannypal.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMerchantViewModel @Inject constructor(private val addMerchantUseCase: AddMerchantUseCase): ViewModel() {

    fun addMerchant(merchant: Merchant, onSuccess: ()-> Unit, onFail: (String) -> Unit) {

        if(merchant.name.isNullOrEmpty())
        {
            onFail(ErrorMessage.MERCHANT_NAME_EMPTY)
        }else{
            viewModelScope.launch {
                addMerchantUseCase
                    .addMerchant(merchant)
                    .collect{
                        when(it)
                        {
                            is Resource.Loading -> {}
                            is Resource.Success -> onSuccess()
                            is Resource.Error -> onFail(ErrorMessage.MERCHANT_EXIST)
                        }
                    }
            }
        }

    }
}