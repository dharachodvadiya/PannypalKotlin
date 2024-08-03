package com.indie.apps.pannypal.data.module

class MerchantDataWithName(
    val id: Long,
    val merchantId : Long,
    val merchantName: String,
    val dateInMilli: Long,
    val details: String? = null,
    val amount: Double,
    val type: Int
)