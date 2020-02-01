package com.remotearthsolutions.expensetracker.utils

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.remotearthsolutions.expensetracker.BuildConfig

object FirebaseEventLogUtils {
    fun logCustom(context: Context, message: String) {
        if (!BuildConfig.DEBUG) {
            val bundle = Bundle()
            bundle.putString("event_name", message)
            FirebaseAnalytics.getInstance(context).logEvent("custom_log", bundle)
        }
    }
}