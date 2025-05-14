package com.indie.apps.pennypal.data.module.purchase

import com.android.billingclient.api.ProductDetails

data class ProductUiModel(
    val productDetails: ProductDetails,
    val isPurchased: Boolean
)