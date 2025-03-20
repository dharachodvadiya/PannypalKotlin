package com.indie.apps.pennypal.data.module.category

import com.indie.apps.pennypal.data.module.CurrencyCountry

data class CategoryAmount(
    val id: Long = 0,
    val name: String,
    val amount: Double,
    val type: Int,
    val iconId: Int,
    val iconColorId: Int,
    val baseCurrencySymbol: String = "",
    val baseCurrencyCountryCode: String = ""
)

fun CategoryAmount.toCurrencyCountry(toCode: String) =
    CurrencyCountry(baseCurrencyCountryCode, toCode)

fun List<CategoryAmount>.mergeAndSortByAmount(): List<CategoryAmount> {
    return this.groupBy { it.name }
        .map { (name, items) ->
            val firstItem = items.first() // Take first item to retain other fields
            val totalAmount = items.sumOf { it.amount } // Sum all amounts for same name

            firstItem.copy(amount = totalAmount) // Create new object with summed amount
        }
        .sortedByDescending { it.amount } // Sort in descending order of amount
}

