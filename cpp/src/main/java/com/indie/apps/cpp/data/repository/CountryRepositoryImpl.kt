package com.indie.apps.cpp.data.repository

import com.indie.apps.cpp.data.CountryDb
import com.indie.apps.cpp.data.model.Country
import com.indie.apps.cpp.data.utils.getFlags
import com.indie.apps.cpp.data.utils.getNumberHint

class CountryRepositoryImpl(private val countryDb: CountryDb) : CountryRepository {
    override fun getFlagsFromCountryCode(countryCode: String) = getFlags(countryCode.lowercase())

    override fun getNumberHintCountryCode(countryCode: String) =
        getNumberHint(countryCode.lowercase())

    override fun getSymbolFromCurrencyCode(currencyCode: String): String {
        return countryDb.countryData.first {
            it.currencyCode.lowercase() == currencyCode.lowercase()
        }.currencySymbol
    }

    override fun getCurrencySymbolFromCountryCode(countryCode: String): String {
        return countryDb.countryData.first {
            it.countryCode.lowercase() == countryCode.lowercase()
        }.currencySymbol
    }

    override fun getDialCodeFromCountryCode(countryCode: String): String {
        return countryDb.countryData.first {
            it.countryCode.lowercase() == countryCode.lowercase()
        }.dialCode
    }

    override fun getCountryCodeFromDialCode(dialCode: String): String {
        return countryDb.countryData.first { it.dialCode == dialCode }.countryCode
    }

    override fun getDefaultCountryCode() = countryDb.defaultCountryCode

    override fun getPhoneCodeFromCountryCode(countryCode: String): String {
        try {
            val defaultCode: String =
                countryDb.countryData.first { it.countryCode.lowercase() == countryCode.lowercase() }.dialCode
            return defaultCode.ifBlank { "+90" }
        } catch (e: Exception) {
            return "+90"
        }
    }

    override fun getCurrencyCodeFromCountryCode(countryCode: String): String {
        try {
            val defaultCode: String =
                countryDb.countryData.first { it.countryCode.lowercase() == countryCode.lowercase() }.currencyCode
            return defaultCode.ifBlank { "USD" }
        } catch (e: Exception) {
            return "USD"
        }
    }

    override fun getCountryCodeFromCurrencyCode(currencyCode: String): String {
        try {
            val defaultCode: String =
                countryDb.countryData.first { it.currencyCode.lowercase() == currencyCode.lowercase() }.countryCode
            return defaultCode.ifBlank { "US" }
        } catch (e: Exception) {
            return "US"
        }
    }

    override fun searchCountry(searchString: String): List<Country> {
        val query = searchString.lowercase().trim()
        return countryDb.countryData.filter { country ->
            with(country) {
                name.contains(query, ignoreCase = true) ||
                        dialCode.contains(query, ignoreCase = true) ||
                        countryCode.contains(query, ignoreCase = true) ||
                        currencyCode.contains(query, ignoreCase = true) ||
                        currencyName.contains(query, ignoreCase = true) ||
                        currencySymbol.contains(query, ignoreCase = true)

            }
        }
    }

}