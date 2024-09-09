package com.indie.apps.pennypal.data.module

data class PaymentWithMode(
    val id: Long = 0,

    val name: String,

    val modeId: Long = 0,

    val preAdded: Int = 0,

    val modeName: String
)

fun PaymentWithMode.toPaymentWithIdName() = PaymentWithIdName(id, name)
