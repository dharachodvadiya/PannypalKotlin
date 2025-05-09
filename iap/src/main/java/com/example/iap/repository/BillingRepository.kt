package com.example.iap.repository

import android.app.Activity
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.example.iap.model.PurchaseResult
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BillingRepository {
    val productDetails: StateFlow<List<ProductDetails>> // all product list
    val purchaseResult: SharedFlow<PurchaseResult> // all product list
    val purchaseStatus: StateFlow<Map<String, Boolean>>
    // val purchases: StateFlow<List<Purchase>> // current purchased detail

    fun initBilling(activity: Activity)
    fun launchPurchaseFlow(activity: Activity, productDetails: ProductDetails)
    fun handleAcknowledgement(purchase: Purchase)
    fun cleanup()

    suspend fun fetchPurchases()
    fun isProductPurchased(productId: String): Boolean
}