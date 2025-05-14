package com.indie.apps.pennypal.presentation.ui.shared_viewmodel.iap

import android.app.Activity
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.example.iap.model.PurchaseResult
import com.example.iap.repository.BillingRepository
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.util.ProductId
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val billingRepository: BillingRepository,
    private val preferenceRepository: PreferenceRepository,
) : ViewModel() {

    private val _productDetailsList = mutableStateListOf<ProductDetails>()

    init {
        connect()
        billingRepository.setOnProductPurchaseFetched { result ->
            if (result is PurchaseResult.Success) {
                result.purchases.forEach { purchase ->
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {

                        if (!purchase.isAcknowledged) {
                            billingRepository.acknowledgePurchase(purchase)
                        }

                        purchase.products.forEach { productId ->
                            setPref(productId, true)
                        }
                    }
                }
            }
        }
    }

    private fun connect() {
        billingRepository.connect { isSuccess ->
            if (isSuccess) checkActiveSubscriptions()
        }
    }

    private suspend fun getProductList() {
        val list = billingRepository.getAllProductDetails()
        _productDetailsList.clear()
        _productDetailsList.addAll(list)
    }

    fun buy(activity: Activity, productId: ProductId) {
        viewModelScope.launch {
            if (_productDetailsList.isEmpty())
                getProductList()
            val productDetails = _productDetailsList.find { it.productId == productId.id }

            if (productDetails != null) {
                billingRepository.launchPurchase(activity, productDetails)
            } else {
                // Optionally notify UI: product not found
            }
        }
    }

    private fun checkActiveSubscriptions() {
        viewModelScope.launch {
            val purchases = billingRepository.getActivePurchases()

            val activeProductIds = purchases
                .filter { it.purchaseState == Purchase.PurchaseState.PURCHASED && it.isAcknowledged }
                .flatMap { it.products } // List<String>

            // All known product IDs (e.g. from enum or config)
            val knownProductIds = ProductId.entries.map { it.id }

            knownProductIds.forEach { id ->
                val isActive = activeProductIds.contains(id)
                setPref(id, isActive)
            }
        }
    }

    private fun setPref(id: String, isActive: Boolean) {
        preferenceRepository.putBoolean("${Util.PREF_PURCHASE_ID}_$id", isActive)
    }


}