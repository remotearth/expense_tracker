package com.remotearthsolutions.expensetracker.utils

import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.remotearthsolutions.expensetracker.BuildConfig

object FabricAnswersUtils {
    fun logCustom(message: String?) {
        if (!BuildConfig.DEBUG) {
            Answers.getInstance()
                .logCustom(CustomEvent(message))
        }
    }
}