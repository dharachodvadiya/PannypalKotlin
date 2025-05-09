package com.example.iap.model

import com.android.billingclient.api.Purchase

sealed class PurchaseResult {
    data class Success(val purchases: List<Purchase>) : PurchaseResult()
    data class Failure(val responseCode: Int, val message: String) : PurchaseResult()
}