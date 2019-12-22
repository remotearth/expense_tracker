package com.remotearthsolutions.expensetracker.activities

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.remotearthsolutions.expensetracker.BuildConfig
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import io.fabric.sdk.android.Fabric
import org.solovyev.android.checkout.Billing
import org.solovyev.android.checkout.Billing.DefaultConfiguration

class ApplicationObject : MultiDexApplication(), ActivityLifecycleCallbacks {
    var isActivityVisible = false
        private set
    var isPremium = false
    var isAppShouldShowAds = false
        private set
    var currentActivity: Activity? = null
        private set
    val billing = Billing(this, object : DefaultConfiguration() {
        override fun getPublicKey(): String {
            return Constants.LICENSE_KEY
        }
    })

    override fun onCreate() {
        super.onCreate()
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics(), Answers())
        }
        SharedPreferenceUtils.getInstance(this)
        registerActivityLifecycleCallbacks(this)
    }

    fun appShouldShowAds(state: Boolean) {
        isAppShouldShowAds = state
    }

    fun activityResumed() {
        isActivityVisible = true
    }

    fun activityPaused() {
        isActivityVisible = false
    }

    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) {
    }

    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        currentActivity = null
    }

    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle
    ) {
    }

    override fun onActivityDestroyed(activity: Activity) {}

    val adProductId: String
        get() = if (BuildConfig.DEBUG) {
            Constants.TEST_PURCHASED_ITEM
        } else {
            Constants.PRODUCT_ID
        }
}