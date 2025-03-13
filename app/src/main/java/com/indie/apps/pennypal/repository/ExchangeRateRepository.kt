package com.indie.apps.pennypal.repository

interface ExchangeRateRepository {
    suspend fun getConversionRate(fromCurrencyCountry: String, toCurrencyCountry: String): Double
    fun getAmountFromRate(amount: Double, rate: Double): Double
}

