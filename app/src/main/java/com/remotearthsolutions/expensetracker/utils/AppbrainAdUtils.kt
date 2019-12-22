package com.remotearthsolutions.expensetracker.utils

import android.app.Activity
import com.appbrain.AdId
import com.appbrain.InterstitialBuilder
import com.appbrain.InterstitialListener
import com.appbrain.InterstitialListener.InterstitialError
import com.remotearthsolutions.expensetracker.activities.ApplicationObject
import com.remotearthsolutions.expensetracker.utils.FabricAnswersUtils.logCustom

class AppbrainAdUtils private constructor(private val activity: Activity) {
    private var interstitialBuilder: InterstitialBuilder? = null
    fun showAds() {
        interstitialBuilder = InterstitialBuilder.create()
            .setAdId(AdId.LEVEL_COMPLETE)
            .setListener(object : InterstitialListener {
                override fun onPresented() {}
                override fun onClick() {}
                override fun onDismissed(b: Boolean) {}
                override fun onAdLoaded() {
                    val app = activity.application as ApplicationObject
                    if (app.isActivityVisible && app.isAppShouldShowAds) {
                        interstitialBuilder!!.show(activity)
                        logCustom("Appbrain Ad shown")
                    }
                }

                override fun onAdFailedToLoad(interstitialError: InterstitialError) {}
            })
            .preload(activity)
    }

    companion object {
        private var instance: AppbrainAdUtils? = null
        fun getInstance(activity: Activity): AppbrainAdUtils? {
            if (instance == null) {
                instance =
                    AppbrainAdUtils(activity)
            }
            return instance
        }
    }

}