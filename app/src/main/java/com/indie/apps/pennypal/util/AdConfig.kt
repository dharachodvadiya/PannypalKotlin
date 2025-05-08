package com.indie.apps.pennypal.util

import com.indie.apps.pennypal.BuildConfig

object AdConfig {
    // Test Ad Unit IDs (used during development)
    private const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    private const val TEST_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
    private const val TEST_NATIVE_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"

    // Production Ad Unit IDs (replace with your PennyPal ad unit IDs)
    private const val PROD_BANNER_AD_UNIT_ID =
        "ca-app-pub-1234567890123456/1111111111" // PennyPal_Banner
    private const val PROD_INTERSTITIAL_AD_UNIT_ID =
        "ca-app-pub-1234567890123456/2222222222" // PennyPal_Interstitial
    private const val PROD_NATIVE_AD_UNIT_ID =
        "ca-app-pub-1234567890123456/3333333333" // PennyPal_Native

    val BANNER_AD_UNIT_ID: String
        get() = if (BuildConfig.DEBUG) TEST_BANNER_AD_UNIT_ID else PROD_BANNER_AD_UNIT_ID

    val INTERSTITIAL_AD_UNIT_ID: String
        get() = if (BuildConfig.DEBUG) TEST_INTERSTITIAL_AD_UNIT_ID else PROD_INTERSTITIAL_AD_UNIT_ID

    val NATIVE_AD_UNIT_ID: String
        get() = if (BuildConfig.DEBUG) TEST_NATIVE_AD_UNIT_ID else PROD_NATIVE_AD_UNIT_ID

}