package com.indie.apps.pennypal.util.app_enum

import android.database.sqlite.SQLiteConstraintException
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.UiText
import java.io.IOException

sealed class AppError(val message: String) {
    data object NetworkError : AppError("Network Not Connected")
    data object DatabaseError : AppError("Database Error")
    data object ConversionError : AppError("Conversion Error")
    data object ServerError : AppError("Server error")
    data object ParsingError : AppError("Failed to parse response")
    data object UnknownError : AppError("Unknown error")
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
    val PAYMENT_TYPE_EMPTY = UiText.StringResource(R.string.enter_payment_type)
    val CATEGORY_NAME_EMPTY = UiText.StringResource(R.string.enter_category_name)

    val MERCHANT_EXIST = UiText.StringResource(R.string.merchant_name_already_used)
    val PAYMENT_TYPE_EXIST = UiText.StringResource(R.string.payment_name_already_used)
    val CATEGORY_EXIST = UiText.StringResource(R.string.category_name_already_used)
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
    val EXCEED_DATE_PERIOD = UiText.StringResource(R.string.date_range_exceed_6_month)
    val CATEGORY_LIMIT = UiText.StringResource(R.string.category_limit_within_total_budget)
    val BUDGET_TITLE_EMPTY = UiText.StringResource(R.string.enter_budget_title)
}