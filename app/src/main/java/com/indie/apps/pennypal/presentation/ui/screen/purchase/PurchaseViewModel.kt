package com.indie.apps.pennypal.presentation.ui.screen.purchase

import android.app.Activity
import android.os.Bundle
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.example.iap.model.PurchaseResult
import com.example.iap.repository.BillingRepository
import com.indie.apps.pennypal.data.module.purchase.ProductUiModel
import com.indie.apps.pennypal.repository.AnalyticRepository
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.util.ProductId
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchaseViewModel @Inject constructor(
    private val billingRepository: BillingRepository,
    private val preferenceRepository: PreferenceRepository,
    private val analyticRepository: AnalyticRepository
) : ViewModel() {

    private val _productDetailsList = mutableStateListOf<ProductDetails>()
    private val _productUiList = mutableStateListOf<ProductUiModel>()
    val productUiList: List<ProductUiModel> get() = _productUiList

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
                            logEvent("purchase_success", Bundle().apply {
                                putString("product_id", productId)
                            })
                        }
                    }
                }
                buildProductUiList()
            }
        }
    }

    fun logEvent(name: String, params: Bundle? = null) {
        analyticRepository.logEvent(name, params)
    }

    private fun connect() {
        billingRepository.connect { isSuccess ->
            if (isSuccess) checkActiveSubscriptions()
            getProductList()
        }
    }

    private fun getProductList() {
        viewModelScope.launch {

            val list = billingRepository.getAllProductDetails()
            _productDetailsList.clear()
            _productDetailsList.addAll(list)
            buildProductUiList()
        }
    }

    fun buy(activity: Activity, productId: ProductId) {
        logEvent("purchase_started", Bundle().apply {
            putString("product_id", productId.id)
        })
        val productDetails = _productDetailsList.find { it.productId == productId.id }

        if (productDetails != null) {
            billingRepository.launchPurchase(activity, productDetails)
        } else {
            // Optionally notify UI: product not found
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

            buildProductUiList()
        }
    }

    private fun setPref(id: String, isActive: Boolean) {
        preferenceRepository.putBoolean("${Util.PREF_PURCHASE_ID}_$id", isActive)
    }

    fun restorePurchases(onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val purchases = billingRepository.getActivePurchases()

                val activeProductIds = mutableListOf<String>()
                purchases.forEach { purchase ->
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        if (!purchase.isAcknowledged) {
                            billingRepository.acknowledgePurchase(purchase)
                        }
                        purchase.products.forEach { productId ->
                            activeProductIds.add(productId)
                            setPref(productId, true)
                        }
                    }
                }

                // Update preferences for all known product IDs
                val knownProductIds = ProductId.entries.map { it.id }
                knownProductIds.forEach { id ->
                    val isActive = activeProductIds.contains(id)
                    setPref(id, isActive)
                }
                buildProductUiList()

                if (activeProductIds.isNotEmpty()) {
                    logEvent("purchase_restored", Bundle().apply {
                        putString("product_id", activeProductIds.toString())
                    })
                    onSuccess("Successfully Restored")
                } else {
                    onSuccess("No Subscription Found")
                }
            } catch (e: Exception) {
                onSuccess("Failed to restore purchases")
            }
        }
    }

    private fun buildProductUiList() {
        val allowedProductIds = ProductId.entries.map { it.id }
        _productUiList.clear()
        _productUiList.addAll(
            _productDetailsList
                .filter { details -> details.productId in allowedProductIds }
                .map { details ->
                    ProductUiModel(details, getIsActive(details.productId))
                }
        )
    }


    private fun getIsActive(id: String) =
        preferenceRepository.getBoolean("${Util.PREF_PURCHASE_ID}_$id", false)


}