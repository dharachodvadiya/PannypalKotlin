package com.indie.apps.pennypal.repository

interface ExchangeRateRepository {
    suspend fun getConversionRate(fromCurrency: String, toCurrency: String): Double
    suspend fun getConvertedAmount(amount: Double, fromCurrency: String, toCurrency: String): Double
}

