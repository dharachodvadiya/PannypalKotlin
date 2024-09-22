package com.indie.apps.pennypal.util

import com.indie.apps.pennypal.R

internal fun getPaymentModeIcon(paymentName: String) : Int {
    return when(paymentName){
        "Other" -> R.drawable.ic_payment
        "Cash" -> R.drawable.ic_cash
        "Bank" -> R.drawable.ic_bank
        "Card" -> R.drawable.ic_card
        "Cheque" -> R.drawable.ic_cheque
        "Net-banking" -> R.drawable.ic_net_banking
        "Upi" -> R.drawable.ic_upi
        else -> R.drawable.ic_payment
    }
}