package com.indie.apps.pennypal.repository

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class InAppAdsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : InAppAdsRepository {

    private var interstitialRequestCount = 0
    private val interstitialTriggerFrequency = 5 // Show ad every given requests


    // Interstitial Ad
    private var interstitialAd: InterstitialAd? = null
    private var interstitialAdLoadCallback: InterstitialAdLoadCallback? = null
    private var interstitialAdShowCount = 0
    private val maxInterstitialAdsPerSession = 2

    // Native Ad
    private var nativeAd: NativeAd? = null
    private var nativeAdShowCount = 0
    private val maxNativeAdsPerList = 2

    data class AdState(
        val isInterstitialReady: Boolean = false,
        val interstitialError: String? = null,
        val bannerAdView: AdView? = null,
        val bannerError: String? = null,
        val nativeAd: NativeAd? = null,
        val nativeError: String? = null
    )

    private var currentState = AdState()

    // --- Banner Ad ---
    override fun loadBannerAd(
        adUnitId: String,
        onStateChange: (AdState) -> Unit
    ): AdView {
        val adView = AdView(context).apply {
            setAdSize(AdSize.BANNER)
            this.adUnitId = adUnitId
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    currentState = currentState.copy(bannerAdView = this@apply, bannerError = null)
                    onStateChange(currentState)
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    currentState =
                        currentState.copy(bannerAdView = null, bannerError = error.message)
                    onStateChange(currentState)
                }
            }
            loadAd(AdRequest.Builder().build())
        }
        return adView
    }


    // Load an interstitial
    override fun loadInterstitialAd(
        adUnitId: String,
        onStateChange: (AdState) -> Unit
    ) {
        if (interstitialAd != null)
            return
        if (interstitialAdShowCount >= maxInterstitialAdsPerSession) {
            currentState = currentState.copy(isInterstitialReady = false)
            onStateChange(currentState)
            return
        }

        interstitialAdLoadCallback = object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                interstitialAd = ad
                currentState =
                    currentState.copy(isInterstitialReady = true, interstitialError = null)
                onStateChange(currentState)
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                interstitialAd = null
                currentState = currentState.copy(
                    isInterstitialReady = false,
                    interstitialError = error.message
                )
                onStateChange(currentState)
            }
        }

        InterstitialAd.load(
            context,
            adUnitId,
            AdRequest.Builder().build(),
            interstitialAdLoadCallback!!
        )
    }

    // Check if an ad should be shown
    private fun shouldShowInterstitialAd(): Boolean {
        return interstitialAd != null && interstitialAdShowCount < maxInterstitialAdsPerSession
    }

    // Show the ad using Activity
    override fun showInterstitialAd(activity: Activity, onAdDismissed: () -> Unit) {

        interstitialRequestCount++

        if (interstitialRequestCount < interstitialTriggerFrequency) {
            onAdDismissed()
            return
        }


        if (shouldShowInterstitialAd() && interstitialAd != null) {

            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    interstitialAdShowCount++
                    interstitialRequestCount = 0 // Reset counter after showing
                    currentState = currentState.copy(isInterstitialReady = false)
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    currentState = currentState.copy(
                        isInterstitialReady = false,
                        interstitialError = adError.message
                    )
                    onAdDismissed()
                }
            }

            interstitialAd?.show(activity)

        } else {
            onAdDismissed()
        }
    }

    override fun loadNativeAd(
        adUnitId: String,
        onStateChange: (AdState) -> Unit
    ) {
        if (nativeAdShowCount >= maxNativeAdsPerList) {
            currentState = currentState.copy(nativeAd = null)
            onStateChange(currentState)
            return
        }

        val adLoader = com.google.android.gms.ads.AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad: NativeAd ->
                nativeAd = ad
                nativeAdShowCount++
                currentState = currentState.copy(nativeAd = ad, nativeError = null)
                onStateChange(currentState)
            }
            .withAdListener(object : com.google.android.gms.ads.AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    nativeAd = null
                    currentState = currentState.copy(nativeAd = null, nativeError = error.message)
                    onStateChange(currentState)
                }
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }


}

