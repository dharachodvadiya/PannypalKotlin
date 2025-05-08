package com.indie.apps.pennypal.presentation.ui.screen

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.AdView
import com.indie.apps.pennypal.repository.InAppAdsRepository
import com.indie.apps.pennypal.repository.InAppAdsRepositoryImpl.AdState
import com.indie.apps.pennypal.util.AdConfig.BANNER_AD_UNIT_ID
import com.indie.apps.pennypal.util.AdConfig.INTERSTITIAL_AD_UNIT_ID
import com.indie.apps.pennypal.util.AdConfig.NATIVE_AD_UNIT_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdViewModel @Inject constructor(
    private val adRepository: InAppAdsRepository
) : ViewModel() {

    fun loadBannerAd(onStateChange: (AdState) -> Unit = {}): AdView {
        return adRepository.loadBannerAd(BANNER_AD_UNIT_ID, onStateChange)
    }

    fun loadInterstitialAd() {
        viewModelScope.launch {
            adRepository.loadInterstitialAd(INTERSTITIAL_AD_UNIT_ID) { }
        }
    }

    fun showInterstitialAd(
        activity: Activity,
        isReload: Boolean = false,
        onAdDismissed: () -> Unit
    ) {
        viewModelScope.launch {
            adRepository.showInterstitialAd(activity) {
                if (isReload) {
                    loadInterstitialAd()
                }
                onAdDismissed()
            }
        }
    }

    fun loadNativeAd() {
        viewModelScope.launch {
            adRepository.loadNativeAd(NATIVE_AD_UNIT_ID) {}
        }
    }
}