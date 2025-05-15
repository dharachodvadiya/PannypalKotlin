package com.indie.apps.pennypal.presentation.ui.shared_viewmodel.ads

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.AdView
import com.indie.apps.pennypal.repository.AnalyticRepository
import com.indie.apps.pennypal.repository.InAppAdsRepository
import com.indie.apps.pennypal.repository.InAppAdsRepositoryImpl.AdState
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.util.AdConfig.BANNER_AD_UNIT_ID
import com.indie.apps.pennypal.util.AdConfig.INTERSTITIAL_AD_UNIT_ID
import com.indie.apps.pennypal.util.AdConfig.NATIVE_AD_UNIT_ID
import com.indie.apps.pennypal.util.ProductId
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdViewModel @Inject constructor(
    private val adRepository: InAppAdsRepository,
    private val preferenceRepository: PreferenceRepository,
    private val analyticRepository: AnalyticRepository,
) : ViewModel() {
    private val noAdsId = "${Util.PREF_PURCHASE_ID}_${ProductId.NoAds.id}"
    fun loadBannerAd(onStateChange: (AdState) -> Unit = {}): AdView? {
        return if (!isSubscribed(noAdsId)) {
            logEvent("ads_show_banner")
            adRepository.loadBannerAd(BANNER_AD_UNIT_ID, onStateChange)
        } else
            null
    }

    fun loadInterstitialAd() {
        if (!isSubscribed(noAdsId)) {
            viewModelScope.launch {
                adRepository.loadInterstitialAd(INTERSTITIAL_AD_UNIT_ID) { }
            }
        }
    }

    fun showInterstitialAd(
        activity: Activity,
        isReload: Boolean = false,
        onAdDismissed: () -> Unit
    ) {
        if (!isSubscribed(noAdsId)) {
            viewModelScope.launch {
                logEvent("ads_show_interstitial")
                adRepository.showInterstitialAd(activity) {
                    if (isReload) {
                        loadInterstitialAd()
                    }
                    onAdDismissed()
                }
            }
        } else {
            onAdDismissed()
        }
    }

    fun loadNativeAd() {
        viewModelScope.launch {
            adRepository.loadNativeAd(NATIVE_AD_UNIT_ID) {}
        }
    }

    private fun isSubscribed(productId: String): Boolean {
        return preferenceRepository.getBoolean(productId, false)
    }

    fun logEvent(name: String, params: Bundle? = null) {
        analyticRepository.logEvent(name, params)
    }
}