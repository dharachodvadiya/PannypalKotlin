package com.indie.apps.pennypal.data.module

class MerchantDataWithName(
    val id: Long,
    val merchantId : Long,
    val merchantName: String,
    val dateInMilli: Long,
    val details: String? = null,
    val amount: Double,
    val type: Int,
    val day: String,
)