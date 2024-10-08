package com.indie.apps.pennypal.data.module

class MerchantDataWithPaymentName(
    val id: Long,
    val paymentId: Long,
    val paymentName: String,
    val dateInMilli: Long,
    val details: String? = null,
    val amount: Double,
    val type: Int
)