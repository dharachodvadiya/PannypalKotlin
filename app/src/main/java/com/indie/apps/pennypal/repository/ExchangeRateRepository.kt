package com.indie.apps.pennypal.repository

interface ExchangeRateRepository {
    suspend fun getConversionRate(fromCurrency: String, toCurrency: String): Double
}

