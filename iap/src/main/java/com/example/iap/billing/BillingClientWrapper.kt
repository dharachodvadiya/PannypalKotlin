package com.example.iap.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.ConnectionState
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryPurchasesAsync
import com.example.iap.model.Product
import com.example.iap.model.ProductType
import com.example.iap.model.PurchaseResult

class BillingClientWrapper(
    private val context: Context,
    private val products: List<Product>
) {

    private val billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(::onPurchasesUpdated)
        .enablePendingPurchases()
        .build()
    //private val _purchaseUpdates = MutableStateFlow<List<Purchase>>(emptyList())
    // val purchaseUpdates: StateFlow<List<Purchase>> = _purchaseUpdates

    private var onProductDetailsFetched: ((List<ProductDetails>) -> Unit)? = null
    private var onProductPurchased: ((PurchaseResult) -> Unit)? = null

    fun onConnect(onConnectSuccess: () -> Unit) {
        if (billingClient.connectionState == ConnectionState.DISCONNECTED) {
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(result: BillingResult) {
                    if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                        queryProductDetails()
                        onConnectSuccess()
                    }
                }

                override fun onBillingServiceDisconnected() {
                    println("Billing service disconnected")
                }
            })
        }
    }

    private fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            // _purchaseUpdates.value = purchases
            onProductPurchased?.invoke(PurchaseResult.Success(purchases))
        } else {
            onProductPurchased?.invoke(
                PurchaseResult.Failure(
                    billingResult.responseCode,
                    billingResult.debugMessage
                )
            )
            println("Purchase failed: ${billingResult.debugMessage}")
        }
    }

    fun launchPurchase(activity: Activity, productDetails: ProductDetails) {
        val offerToken = productDetails.subscriptionOfferDetails?.firstOrNull()?.offerToken

        val params = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(productDetails)
            .apply {
                offerToken?.let { setOfferToken(it) }
            }
            .build()

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(params))
            .build()

        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    fun setOnProductDetailsFetched(callback: (List<ProductDetails>) -> Unit) {
        onProductDetailsFetched = callback
    }

    fun setOnProductPurchaseFetched(callback: (PurchaseResult) -> Unit) {
        onProductPurchased = callback
    }

    private fun queryProductDetails() {
        val inApp = products.filter { it.type == ProductType.INAPP }
        val subs = products.filter { it.type == ProductType.SUBS }

        val all = (inApp + subs).map {
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(it.id)
                .setProductType(
                    if (it.type == ProductType.INAPP) BillingClient.ProductType.INAPP
                    else BillingClient.ProductType.SUBS
                ).build()
        }

        billingClient.queryProductDetailsAsync(
            QueryProductDetailsParams.newBuilder().setProductList(all).build()
        ) { result, details ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                onProductDetailsFetched?.invoke(details)
            }
        }
    }

    fun acknowledgePurchase(purchase: Purchase) {
        if (!purchase.isAcknowledged) {
            val params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken).build()
            billingClient.acknowledgePurchase(params) { result ->
                println("Acknowledge result: ${result.responseCode}")
            }
        }
    }

    suspend fun queryPurchases(): List<Purchase> {
        val inAppResult = billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )
        val subsResult = billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        )

        val purchases = mutableListOf<Purchase>()
        if (inAppResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            purchases.addAll(
                inAppResult.purchasesList.filter { purchase ->
                    purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                }
            )
        }
        if (subsResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            purchases.addAll(
                subsResult.purchasesList.filter { purchase ->
                    purchase.purchaseState == Purchase.PurchaseState.PURCHASED &&
                            purchase.isAutoRenewing
                }
            )
        }

        return purchases
    }


    fun endConnection() = billingClient.endConnection()
}
