package com.indie.apps.pennypal.data.module

data class UserWithPaymentName(
    val name: String,
    val email: String? = null,
    val currency: String,
    val paymentId: Long = 1L,
    val paymentName: String,
    val currencyCountryCode: String,

    )