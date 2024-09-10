package com.indie.apps.cpp.data.repository

import com.indie.apps.cpp.data.model.Country

interface CountryRepository {

    fun getFlagsFromCountryCode(countryCode: String): Int
    fun getNumberHintCountryCode(countryCode: String): Int
    fun getSymbolFromCurrencyCode(currencyCode: String): String
    fun getDialCodeFromCountryCode(countryCode: String): String
    fun getCountryCodeFromDialCode(dialCode: String): String
    fun getDefaultCountryCode(): String
    fun getPhoneCodeFromCountryCode(countryCode: String): String
    fun getCurrencyCodeFromCountryCode(countryCode: String): String
    fun getCountryCodeFromCurrencyCode(currencyCode: String): String
    fun searchCountryForDialCode(searchString: String): List<Country>
    fun searchCountryForCurrency(searchString: String): List<Country>
}