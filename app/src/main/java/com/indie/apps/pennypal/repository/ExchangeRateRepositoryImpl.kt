package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.BuildConfig
import com.indie.apps.pennypal.data.database.dao.ExchangeRateDao
import com.indie.apps.pennypal.data.database.entity.ExchangeRate
import com.indie.apps.pennypal.data.service.ExchangeRateApiService
import java.util.Calendar
import javax.inject.Inject


class ExchangeRateRepositoryImpl @Inject constructor(
    private val apiService: ExchangeRateApiService,
    private val exchangeRateDao: ExchangeRateDao
) : ExchangeRateRepository {

    private val apiKey = BuildConfig.EXCHANGE_RATE_API_KEY

    override suspend fun getConversionRate(fromCurrency: String, toCurrency: String): Double {
        val lastUpdate = exchangeRateDao.getLastUpdateTime() ?: 0L
        val now = System.currentTimeMillis()

        // Check if last update is from a different day
        if (!isSameDay(lastUpdate, now)) {
            // Fetch from API and cache
            val response = apiService.getLatestRates(apiKey, toCurrency)
            if (response.isSuccessful) {
                val rates = response.body()?.conversion_rates ?: emptyMap()
                rates.forEach { (currency, rate) ->
                    exchangeRateDao.insert(
                        ExchangeRate(
                            fromCurrency = fromCurrency,
                            toCurrency = currency,
                            rate = rate,
                            lastUpdated = now
                        )
                    )
                }
            }
        }

        // Get cached rate
        return exchangeRateDao.getRate(fromCurrency, toCurrency)?.rate ?: 1.0
    }

    private fun isSameDay(time1: Long, time2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = time1 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = time2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

}