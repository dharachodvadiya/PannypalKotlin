package com.indie.apps.pennypal.util

import android.database.sqlite.SQLiteConstraintException
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.UiText
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
    val USER_NAME_EMPTY = UiText.StringResource(R.string.enter_name)
    val MERCHANT_NAME_EMPTY = UiText.StringResource(R.string.enter_merchant_name)
    val AMOUNT_EMPTY = UiText.StringResource(R.string.enter_amount)
    val AMOUNT_PAYMENT_TYPE = UiText.StringResource(R.string.enter_payment_type)

    val MERCHANT_EXIST = UiText.StringResource(R.string.merchant_name_already_used)
    val PAYMENT_TYPE_EXIST = UiText.StringResource(R.string.payment_name_already_used)
    val PHONE_NO_INVALID = UiText.StringResource(R.string.enter_valid_phone_number)
    val SELECT_MERCHANT = UiText.StringResource(R.string.select_merchant)
    val SELECT_PAYMENT = UiText.StringResource(R.string.select_payment)
    val SELECT_CATEGORY = UiText.StringResource(R.string.select_category)

    val SELECT_MONTH = UiText.StringResource(R.string.select_month)
    val SELECT_YEAR = UiText.StringResource(R.string.select_year)
    val SELECT_DATE = UiText.StringResource(R.string.select_date)
    val BUDGET_EXIST_MONTH = UiText.StringResource(R.string.budget_exist_for_this_month)
    val BUDGET_EXIST_YEAR = UiText.StringResource(R.string.budget_exist_for_this_year)
    val INCORRECT_DATE = UiText.StringResource(R.string.from_date_greater_than_to_date)
    val CATEGORY_LIMIT = UiText.StringResource(R.string.category_limit_within_total_budget)
    val BUDGET_TITLE_EMPTY = UiText.StringResource(R.string.enter_budget_title)
}