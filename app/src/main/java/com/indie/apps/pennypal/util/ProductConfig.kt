package com.indie.apps.pennypal.util

import com.example.iap.model.Product
import com.example.iap.model.ProductType

enum class ProductId(val id: String) {
    NoAds("text_no_ads_minimum_time")
}

object ProductConfig {


    val products = listOf(
        Product(id = "test_no_ads", type = ProductType.INAPP),
        Product(id = ProductId.NoAds.id, type = ProductType.SUBS)
    )

    val productList = listOf(
        Product(id = "no_ads_1_month", type = ProductType.SUBS, isConsumable = false),
        Product(id = "no_ads_2_months", type = ProductType.SUBS, isConsumable = false),
        Product(id = "no_ads_3_months", type = ProductType.SUBS, isConsumable = false),
        Product(
            id = "no_ads_lifetime",
            type = ProductType.INAPP,
            isConsumable = false
        ), // Non-consumable (permanent)
        Product(
            id = "pro_app_version",
            type = ProductType.INAPP,
            isConsumable = false
        ) // Non-consumable (permanent)
    )


}