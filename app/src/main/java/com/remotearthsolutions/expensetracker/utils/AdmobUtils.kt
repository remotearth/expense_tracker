package com.remotearthsolutions.expensetracker.utils

import android.app.Activity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.remotearthsolutions.expensetracker.BuildConfig
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.ApplicationObject
import com.remotearthsolutions.expensetracker.utils.FirebaseEventLogUtils.logCustom

class AdmobUtils private constructor(private val activity: Activity) {
    private var interstitialAd: InterstitialAd = InterstitialAd(activity)

    fun showInterstitialAds() {
        interstitialAd.let {
            if (BuildConfig.DEBUG) {
                it.adUnitId = activity.getString(R.string.admob_test_ad_id)
            } else {
                it.adUnitId = activity.getString(R.string.admob_ad_id)
            }
            val adRequest =
                AdRequest.Builder().build()
            it.loadAd(adRequest)
            it.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    val app = activity.application as ApplicationObject
                    if (app.isActivityVisible && app.isAppShouldShowAds) {
                        it.show()
                        logCustom(activity,"Ad shown")
                    }
                }
            }
        }
    }

    companion object : SingletonHolder<AdmobUtils, Activity>(::AdmobUtils)
}