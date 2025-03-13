package com.indie.apps.pennypal.repository

import com.indie.apps.cpp.data.repository.CountryRepository
import com.indie.apps.pennypal.BuildConfig
import com.indie.apps.pennypal.data.database.dao.ExchangeRateDao
import com.indie.apps.pennypal.data.database.entity.ExchangeRate
import com.indie.apps.pennypal.data.service.ExchangeRateApiService
import java.util.Calendar
import javax.inject.Inject


class ExchangeRateRepositoryImpl @Inject constructor(
    private val apiService: ExchangeRateApiService,
    private val exchangeRateDao: ExchangeRateDao,
    private val countryRepository: CountryRepository
) : ExchangeRateRepository {

    private val apiKey = BuildConfig.EXCHANGE_RATE_API_KEY

    override suspend fun getConversionRate(fromCurrencyCountry: String, toCurrencyCountry: String): Double {
        if (fromCurrencyCountry == toCurrencyCountry)
            return 1.0;



        val fromCurrency = countryRepository.getCurrencyCodeFromCountryCode(fromCurrencyCountry)
        val toCurrency = countryRepository.getCurrencyCodeFromCountryCode(toCurrencyCountry)

        val exchangeRateFromDb = exchangeRateDao.getCurrencyFromFromCurrency(fromCurrency)
        val isLoadNeed = if (exchangeRateFromDb?.toCurrency != toCurrency) true else false
        val lastUpdate = exchangeRateDao.getLastUpdateTime() ?: 0L
        val now = System.currentTimeMillis()

        // Check if last update is from a different day
        if (!isSameDay(lastUpdate, now) || isLoadNeed) {
            // Fetch from API and cache
            val response = apiService.getLatestRates(apiKey, toCurrency)

            if (response.isSuccessful) {

                exchangeRateDao.deleteAll()
                val rates = response.body()?.conversion_rates ?: emptyMap()
                val baseCurrency = response.body()?.base_code ?: toCurrency

                rates.forEach { (currency, rate) ->
                    exchangeRateDao.insert(
                        ExchangeRate(
                            fromCurrency = currency,
                            toCurrency = baseCurrency,
                            rate = rate,
                            lastUpdated = now
                        )
                    )
                }
            }
        }

        val rate = exchangeRateDao.getRate(fromCurrency, toCurrency)

        // Get cached rate
        return rate?.rate ?: 1.0
    }

    override fun getAmountFromRate(
        amount: Double,
        rate: Double
    ): Double {
        return amount / rate
    }

    private fun isSameDay(time1: Long, time2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = time1 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = time2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

}