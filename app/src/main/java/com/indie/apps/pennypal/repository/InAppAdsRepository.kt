package com.indie.apps.pennypal.repository

import android.app.Activity
import com.google.android.gms.ads.AdView
import com.indie.apps.pennypal.repository.InAppAdsRepositoryImpl.AdState

interface InAppAdsRepository {
    fun loadBannerAd(adUnitId: String, onStateChange: (AdState) -> Unit): AdView

    fun loadInterstitialAd(adUnitId: String, onStateChange: (AdState) -> Unit)
    fun showInterstitialAd(activity: Activity, onAdDismissed: (Boolean) -> Unit)

    fun loadNativeAd(adUnitId: String, onStateChange: (AdState) -> Unit)
}

