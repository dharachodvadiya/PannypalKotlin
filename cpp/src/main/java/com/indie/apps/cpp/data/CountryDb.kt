package com.indie.apps.cpp.data

import android.content.Context
import android.telephony.TelephonyManager
import androidx.compose.ui.text.intl.Locale
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.indie.apps.cpp.data.model.Country
import java.io.IOException

class CountryDb {
    var countryData: List<Country> = emptyList()
    var defaultCountryCode: String = ""

    private fun loadData(context: Context) {
        if (countryData.isEmpty()) {
            defaultCountryCode = getDefaultCountryCode(context)
            try {
                val jsonFileString =
                    context.assets.open("Countries.json").bufferedReader().use { it.readText() }
                val type = object : TypeToken<List<Country>>() {}.type
                countryData = Gson().fromJson(jsonFileString, type)
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }
        }
    }

    private fun getDefaultCountryCode(context: Context): String {
        val localeCode: TelephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCode = localeCode.networkCountryIso
        val defaultLocale = Locale.current.language
        return countryCode.ifBlank { defaultLocale }
    }

    companion object {
        private var INSTANCE: CountryDb? = null

        fun getInstance(context: Context): CountryDb {
            if (INSTANCE == null) {
                INSTANCE = CountryDb()
            }


            INSTANCE!!.loadData(context)
            return INSTANCE!!
        }
    }
}