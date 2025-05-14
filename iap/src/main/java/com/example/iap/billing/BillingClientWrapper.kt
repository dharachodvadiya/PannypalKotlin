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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

class BillingClientWrapper(
    context: Context,
    private val products: List<Product>
) {

    private val billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(::onPurchasesUpdated)
        .enablePendingPurchases()
        .build()

    private var onProductPurchased: ((PurchaseResult) -> Unit)? = null

    fun setOnProductPurchaseFetched(callback: (PurchaseResult) -> Unit) {
        onProductPurchased = callback
    }

    private fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
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

    fun acknowledgePurchase(purchase: Purchase) {
        if (!purchase.isAcknowledged) {
            val params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken).build()
            billingClient.acknowledgePurchase(params) { result ->
                println("Acknowledge result: ${result.responseCode}")
            }
        }
    }

    fun onConnect(onConnectSuccess: (Boolean) -> Unit) {
        if (billingClient.connectionState == ConnectionState.DISCONNECTED) {
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(result: BillingResult) {
                    if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                        onConnectSuccess(true)
                    }
                }

                override fun onBillingServiceDisconnected() {
                    println("Billing service disconnected")
                }
            })
        } else {
            onConnectSuccess(false)
        }
    }

    suspend fun queryAllProductDetails(): List<ProductDetails> {
        val inApp = queryProductDetailsForType(ProductType.INAPP)
        val subs = queryProductDetailsForType(ProductType.SUBS)
        return inApp + subs
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun queryProductDetailsForType(type: ProductType): List<ProductDetails> {
        val productList = products.filter { it.type == type }.map {
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(it.id)
                .setProductType(
                    if (type == ProductType.INAPP)
                        BillingClient.ProductType.INAPP
                    else BillingClient.ProductType.SUBS
                )
                .build()
        }

        if (productList.isEmpty()) return emptyList()

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        return suspendCancellableCoroutine { cont ->
            billingClient.queryProductDetailsAsync(params) { result, productDetails ->
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    cont.resume(productDetails, null)
                } else {
                    cont.resume(emptyList(), null)
                }
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
