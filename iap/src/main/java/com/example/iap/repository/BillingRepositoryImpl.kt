package com.example.iap.repository

import android.app.Activity
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.example.iap.billing.BillingClientWrapper
import com.example.iap.model.PurchaseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BillingRepositoryImpl(
    private val billingClientWrapper: BillingClientWrapper,
    private val dispatcher: CoroutineDispatcher
) : BillingRepository {

    private val _productDetails = MutableStateFlow<List<ProductDetails>>(emptyList())
    override val productDetails: StateFlow<List<ProductDetails>> = _productDetails

    private val _purchaseResults = MutableSharedFlow<PurchaseResult>()
    override val purchaseResult: SharedFlow<PurchaseResult> = _purchaseResults

    // A StateFlow to track the purchase status of products
    private val _purchaseStatus = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    override val purchaseStatus: StateFlow<Map<String, Boolean>> = _purchaseStatus

    // override val purchases: StateFlow<List<Purchase>> = billingClientWrapper.purchaseUpdates

    init {
        billingClientWrapper.onConnect() {
            CoroutineScope(dispatcher).launch {
                fetchPurchases()
            }
        }
    }

    override fun initBilling(activity: Activity) {
        billingClientWrapper.setOnProductDetailsFetched {
            _productDetails.value = it
        }
        billingClientWrapper.setOnProductPurchaseFetched { result ->
            CoroutineScope(dispatcher).launch {
                _purchaseResults.emit(result)
                fetchPurchases()
            }
        }
    }

    override fun launchPurchaseFlow(activity: Activity, productDetails: ProductDetails) {
        billingClientWrapper.launchPurchase(activity, productDetails)
    }

    override fun handleAcknowledgement(purchase: Purchase) {
        billingClientWrapper.acknowledgePurchase(purchase)
    }

    override suspend fun fetchPurchases() {
        withContext(dispatcher) {
            try {
                val purchases = billingClientWrapper.queryPurchases()
                // Handle or emit the purchases if needed
                println("Fetched Purchases: $purchases")
                updatePurchaseStatus(purchases)
            } catch (e: Exception) {
                println("Error fetching purchases: ${e.message}")
            }
        }
    }

    override fun cleanup() {
        billingClientWrapper.endConnection()
    }

    override fun isProductPurchased(productId: String): Boolean {
        return _purchaseStatus.value[productId] ?: false
    }

    private fun updatePurchaseStatus(purchases: List<Purchase>) {
        // Create a map of product ID to purchase status

        val updatedStatus = mutableMapOf<String, Boolean>()
        purchases
            .filter { it.purchaseState == Purchase.PurchaseState.PURCHASED }
            .flatMap { it.products } // Handle multiple product IDs per purchase
            //.filter { productId -> ProductConfig.products.any { it.id == productId } } // Validate
            .forEach { productId ->
                updatedStatus[productId] = true
            }


        // Merge the new status with the existing status
        _purchaseStatus.value = updatedStatus
    }

}