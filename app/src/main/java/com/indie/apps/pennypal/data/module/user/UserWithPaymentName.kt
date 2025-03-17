package com.indie.apps.pennypal.data.module.user

data class UserWithPaymentName(
    val name: String,
    val email: String? = null,
    val paymentId: Long = 1L,
    val paymentName: String,
    val currencyCountryCode: String,

    )