package com.example.iap.repository

import android.app.Activity
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.example.iap.billing.BillingClientWrapper
import com.example.iap.model.PurchaseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class BillingRepositoryImpl(
    private val billingClientWrapper: BillingClientWrapper,
    private val dispatcher: CoroutineDispatcher
) : BillingRepository {

    override fun connect(onConnected: (Boolean) -> Unit) {
        billingClientWrapper.onConnect(onConnected)
    }

    override fun setOnProductPurchaseFetched(callback: (PurchaseResult) -> Unit) {
        billingClientWrapper.setOnProductPurchaseFetched(callback)
    }

    override fun launchPurchase(activity: Activity, productDetails: ProductDetails) {
        billingClientWrapper.launchPurchase(activity, productDetails)
    }

    override suspend fun getActivePurchases() = withContext(dispatcher) {
        billingClientWrapper.queryPurchases()
    }

    override suspend fun getAllProductDetails() = withContext(dispatcher) {
        billingClientWrapper.queryAllProductDetails()
    }

    override suspend fun isProductActive(productId: String): Boolean {
        val purchases = getActivePurchases()
        return purchases.any {
            it.products.contains(productId) &&
                    it.purchaseState == Purchase.PurchaseState.PURCHASED &&
                    it.isAcknowledged
        }
    }

    override fun acknowledgePurchase(purchase: Purchase) {
        billingClientWrapper.acknowledgePurchase(purchase)
    }

    override fun endConnection() {
        billingClientWrapper.endConnection()
    }
}