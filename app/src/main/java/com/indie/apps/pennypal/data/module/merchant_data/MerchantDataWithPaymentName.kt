package com.indie.apps.pennypal.data.module.merchant_data

class MerchantDataWithPaymentName(
    val id: Long,
    val paymentId: Long,
    val paymentName: String,
    val dateInMilli: Long,
    val details: String? = null,
    val amount: Double,
    val type: Int,
    val originalAmount : Double,
    val originalAmountSymbol : String,
)