package com.remotearthsolutions.expensetracker.activities

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.google.firebase.analytics.FirebaseAnalytics
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.remotearthsolutions.expensetracker.BuildConfig
import com.remotearthsolutions.expensetracker.Configs
import com.remotearthsolutions.expensetracker.di.modules.viewModels
import com.remotearthsolutions.expensetracker.utils.LocalNotificationManager
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import com.yariksoffice.lingver.Lingver
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin



class ApplicationObject : MultiDexApplication(), ActivityLifecycleCallbacks {
    var isActivityVisible = false
        private set
    var isPremium = false
    var isAppShouldShowAds = false
        private set
    var currentActivity: Activity? = null
        private set

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ApplicationObject)
            modules(listOf(viewModels))
        }

        appInstance = this

        AppCenter.start(
            this, Configs.getAppCenterAppSecret(this),
            Analytics::class.java, Crashes::class.java
        )

        Lingver.init(this, "en")
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
        SharedPreferenceUtils.getInstance(this)
        registerActivityLifecycleCallbacks(this)
        LocalNotificationManager.createNotificationChannel(this)
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

    companion object {
        var appInstance: ApplicationObject? = null
    }
}