package com.indie.apps.pennypal.data.module

import com.indie.apps.pennypal.data.entity.Payment

data class PaymentWithMode(
    val id: Long = 0,

    val name: String,

    val modeId: Long = 0,

    val preAdded: Int = 0,

    val modeName: String
)

fun PaymentWithMode.toPayment() = Payment(
    id = id,
    name = name,
    modeId = modeId,
    preAdded = preAdded
)
