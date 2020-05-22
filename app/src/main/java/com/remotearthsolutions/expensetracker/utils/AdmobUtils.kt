package com.remotearthsolutions.expensetracker.utils

import android.app.Activity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.remotearthsolutions.expensetracker.BuildConfig
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.ApplicationObject

class AdmobUtils private constructor(private val activity: Activity) {
    private var interstitialAd: InterstitialAd = InterstitialAd(activity)

    init {
        if (BuildConfig.DEBUG) {
            interstitialAd.adUnitId = activity.getString(R.string.admob_test_ad_id)
        } else {
            interstitialAd.adUnitId = activity.getString(R.string.admob_ad_id)
        }
    }

    fun showInterstitialAds() = interstitialAd.let {
        val adRequest = AdRequest.Builder().build()
        it.loadAd(adRequest)
        it.adListener = object : AdListener() {
            override fun onAdLoaded() {
                val app = activity.application as ApplicationObject
                if (app.isActivityVisible && app.isAppShouldShowAds) {
                    it.show()
                    AmplitudeUtils.logEvent(AmplitudeUtils.AD_SHOWN)
                }
            }
        }
    }

    companion object : SingletonHolder<AdmobUtils, Activity>(::AdmobUtils)
}