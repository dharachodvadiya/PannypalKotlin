package com.indie.apps.pennypal.data.module.merchant_data

import com.indie.apps.pennypal.data.module.balance.TotalDaily

data class MerchantDataWithNameWithDayTotal(
    val day: String,
    val totalIncome: Double?,
    val totalExpense: Double?,
    val id: Long?,
    val amount: Double?,
    val details: String?,
    val merchantName: String?,
    val merchantId : Long?,
    val dateInMilli: Long,
    val type: Int?,
)

fun MerchantDataWithNameWithDayTotal.toMerchantDataDailyTotal() = TotalDaily( day, totalIncome ?: 0.0,totalExpense ?:0.0)
fun MerchantDataWithNameWithDayTotal.toMerchantDataWithName() = MerchantDataWithName(id ?: -1, merchantId, merchantName ?: "", dateInMilli, details, amount ?: 0.0, type ?: 1, day  )