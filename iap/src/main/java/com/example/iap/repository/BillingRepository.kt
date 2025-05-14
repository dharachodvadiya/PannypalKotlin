package com.example.iap.repository

import android.app.Activity
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.example.iap.model.PurchaseResult

interface BillingRepository {
    fun connect(onConnected: (Boolean) -> Unit)
    fun setOnProductPurchaseFetched(callback: (PurchaseResult) -> Unit)
    fun launchPurchase(activity: Activity, productDetails: ProductDetails)
    suspend fun getActivePurchases(): List<Purchase>
    suspend fun getAllProductDetails(): List<ProductDetails>
    suspend fun isProductActive(productId: String): Boolean
    fun acknowledgePurchase(purchase: Purchase)
    fun endConnection()
}