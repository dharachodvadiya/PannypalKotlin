package com.indie.apps.pennypal.util

import android.database.sqlite.SQLiteConstraintException
import java.io.IOException

sealed class AppError(val message: String) {
    data object NetworkError : AppError("Network Failure")
    data object DatabaseError : AppError("Database Error")
    data object ConversionError : AppError("Conversion Error")
    // Add more error types as needed
}

fun handleException(e: Throwable): AppError {
    return when (e) {
        is IOException -> AppError.NetworkError
        is SQLiteConstraintException -> AppError.DatabaseError
        else -> AppError.ConversionError
    }
}

object ErrorMessage {
    const val MERCHANT_NAME_EMPTY = "Enter Merchant Name"
    const val AMOUNT_EMPTY = "Enter Amount"
    const val AMOUNT_PAYMENT_TYPE = "Enter Payment Type"

    const val MERCHANT_EXIST = "Merchant Name Already Used"
    const val PAYMENT_TYPE_EXIST = "Payment Name Already Used"
    const val PHONE_NO_INVALID = "Enter Valid Phone Number"
    const val SELECT_MERCHANT = "Select Merchant"
    const val SELECT_PAYMENT = "Select Payment"
    const val SELECT_CATEGORY = "Select Category"

    const val SELECT_MONTH = "Select Month"
    const val SELECT_YEAR = "Select Year"
    const val SELECT_DATE = "Select Date"
    const val INCORRECT = "From Date must be grater than To Date"

}