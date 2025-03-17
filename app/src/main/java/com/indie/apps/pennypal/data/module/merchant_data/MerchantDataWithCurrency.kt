package com.indie.apps.pennypal.data.module.merchant_data

data class MerchantDataWithCurrency(
    val id: Long = 0,

    val merchantId: Long? = null,

    val categoryId: Long,

    val paymentId: Long,

    val dateInMilli: Long,

    val details: String? = null,

    val amount: Double,

    val baseCurrencyId: Long,

    val originalAmount: Double,

    val originalCurrencyId: Long,

    val type: Int,


    )
