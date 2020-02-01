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
    private var interstitialAd: InterstitialAd? = null
    fun showInterstitialAds() {
        interstitialAd = InterstitialAd(activity)
        if (BuildConfig.DEBUG) {
            interstitialAd!!.adUnitId = activity.getString(R.string.admob_test_ad_id)
        } else {
            interstitialAd!!.adUnitId = activity.getString(R.string.admob_ad_id)
        }
        val adRequest =
            AdRequest.Builder().build()
        interstitialAd!!.loadAd(adRequest)
        interstitialAd!!.adListener = object : AdListener() {
            override fun onAdLoaded() {
                val app = activity.application as ApplicationObject
                if (app.isActivityVisible && app.isAppShouldShowAds) {
                    interstitialAd!!.show()
                    logCustom(activity,"Ad shown")
                }
            }
        }
    }

    companion object {
        private var instance: AdmobUtils? = null
        fun getInstance(activity: Activity): AdmobUtils? {
            if (instance == null) {
                instance =
                    AdmobUtils(activity)
            }
            return instance
        }
    }

}