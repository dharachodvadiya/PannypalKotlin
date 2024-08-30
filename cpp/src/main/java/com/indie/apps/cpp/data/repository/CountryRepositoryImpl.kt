package com.indie.apps.cpp.data.repository

import com.indie.apps.cpp.data.CountryDb
import com.indie.apps.cpp.data.model.Country
import com.indie.apps.cpp.data.utils.getFlags
import com.indie.apps.cpp.data.utils.getNumberHint

class CountryRepositoryImpl(private val countryDb: CountryDb) : CountryRepository {
    override fun getFlagsFromCountryCode(countryCode: String) = getFlags(countryCode.lowercase())

    override fun getNumberHintCountryCode(countryCode: String) = getNumberHint(countryCode.lowercase())

    override fun getSymbolFromCurrencyCode(currencyCode: String): String {
        return countryDb.countryData.first {
            it.currencyCode.lowercase() == currencyCode.lowercase()
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

    override fun searchCountryForDialCode(searchString: String): List<Country> {
        val countryList = ArrayList<Country>()
        countryDb.countryData.forEach {
            if (it.name.lowercase().contains(searchString.lowercase()) ||
                it.dialCode.contains(searchString.lowercase())
            ) {
                countryList.add(it)
            }
        }
        return countryList
    }

    override fun searchCountryForCurrency(searchString: String): List<Country> {
        val countryList = ArrayList<Country>()
        countryDb.countryData.forEach {
            if (it.currencyCode.lowercase().contains(searchString.lowercase()) ||
                it.currencyName.lowercase().contains(searchString.lowercase()) ||
                it.name.lowercase().contains(searchString.lowercase())
            ) {
                countryList.add(it)
            }
        }
        return countryList
    }

}