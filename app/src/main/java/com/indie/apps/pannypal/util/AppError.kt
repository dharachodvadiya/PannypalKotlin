package com.indie.apps.pannypal.util

import android.database.sqlite.SQLiteConstraintException
import java.io.IOException

sealed class AppError(val message: String) {
    object NetworkError : AppError("Network Failure")
    object DatabaseError : AppError("Database Error")
    object ConversionError : AppError("Conversion Error")
    // Add more error types as needed
}

fun handleException(e: Throwable): AppError {
    return when (e) {
        is IOException -> AppError.NetworkError
        is SQLiteConstraintException -> AppError.DatabaseError
        else -> AppError.ConversionError
    }
}

object ErrorMessage{
    const val MERCHANT_NAME_EMPTY = "Enter Merchant Name"
    const val MERCHANT_EXIST = "Merchant Already Exist"
    const val PHONE_NO_INVALID = "Enter Valid Phone Number"
    const val PAYMENT_TYPE_EXIST = "Payment Type Exist"
}