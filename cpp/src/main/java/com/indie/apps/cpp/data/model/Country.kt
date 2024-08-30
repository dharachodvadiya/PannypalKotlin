package com.indie.apps.cpp.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class Country(
    @SerializedName("country_name")
    @Expose
    val name: String = "",
    @SerializedName("dial_code")
    @Expose
    val dialCode: String = "",
    @SerializedName("country_code")
    @Expose
    val countryCode: String = "",
    @SerializedName("currency_name")
    @Expose
    val currencyName: String = "",
    @SerializedName("currency_code")
    @Expose
    val currencyCode: String = "",
    @SerializedName("currency_symbol")
    @Expose
    val currencySymbol: String = ""
)