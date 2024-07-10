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