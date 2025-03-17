package com.indie.apps.pennypal.data.module.merchant_data

class MerchantDataWithAllData(
    val id: Long = 0,
    val merchantId: Long?,
    val merchantName: String?,
    val categoryId: Long,
    val categoryName: String,
    val categoryIconId: Int,
    val categoryIconColorId: Int,
    val paymentId: Long,
    val paymentName: String,
    val dateInMilli: Long,
    val details: String? = null,
    val amount: Double,
    val type: Int,
)