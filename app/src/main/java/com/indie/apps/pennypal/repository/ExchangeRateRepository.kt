package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.util.Resource

interface ExchangeRateRepository {
    suspend fun getConversionRate(fromCurrencyCountry: String, toCurrencyCountry: String): Resource<Double>
    fun getAmountFromRate(amount: Double, rate: Double): Double
}

