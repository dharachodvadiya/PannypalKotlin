package com.indie.apps.pennypal.presentation.ui.screen

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.example.iap.repository.BillingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val repository: BillingRepository
) : ViewModel() {

    val productDetails = repository.productDetails
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val purchases = repository.purchaseResult

    fun init(activity: Activity) {
        repository.initBilling(activity)
    }

    fun buy(activity: Activity, product: ProductDetails) =
        repository.launchPurchaseFlow(activity, product)

    fun acknowledge(purchase: Purchase) = repository.handleAcknowledgement(purchase)

    fun isProductPurchased(productId: String): StateFlow<Boolean> {
        val flow = MutableStateFlow(false)
        viewModelScope.launch {
            repository.purchaseStatus.collect { statusMap ->
                flow.value = statusMap[productId] ?: false
            }
        }
        return flow
    }

    override fun onCleared() {
        super.onCleared()
        repository.cleanup()
    }
}