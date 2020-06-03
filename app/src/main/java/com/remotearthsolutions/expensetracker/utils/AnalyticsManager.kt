package com.remotearthsolutions.expensetracker.utils

import com.amplitude.api.Amplitude
import com.flurry.android.FlurryAgent
import com.remotearthsolutions.expensetracker.BuildConfig


object AnalyticsManager {
    const val EXPENSE_TYPE_DEFAULT = "EXPENSE_TYPE_DEFAULT"
    const val EXPENSE_TYPE_SCHEDULED = "EXPENSE_TYPE_SCHEDULED"
    const val AD_SHOWN = "AD_SHOWN"
    const val CLOUD_BACKUP = "CLOUD_BACKUP"
    const val CLOUD_DOWNLOAD = "CLOUD_DOWNLOAD"
    const val DATA_IMPORTED = "DATA_IMPORTED"
    const val DATA_EXPORTED = "DATA_EXPORTED"
    const val EXPORT_REMINDER_ENABLED = "EXPORT_REMINDER_ENABLED"
    const val EXPORT_REMINDER_DISABLED = "EXPORT_REMINDER_DISABLED"
    const val ADD_EXPENSE_DAILY_REMINDER_ENABLED = "ADD_EXPENSE_DAILY_REMINDER_ENABLED"
    const val ADD_EXPENSE_DAILY_REMINDER_DISABLED = "ADD_EXPENSE_DAILY_REMINDER_DISABLED"
    const val AUTOMATIC_SALARY_ENABLED = "AUTOMATIC_SALARY_ENABLED"
    const val AUTOMATIC_SALARY_DISABLED = "AUTOMATIC_SALARY_DISABLED"
    const val REMINDED_TO_ADD_EXPENSE = "REMINDED_TO_ADD_EXPENSE"
    const val AFTER_FIVE_DAYS_REMINDED_TO_ADD_EXPENSE = "AFTER_FIVE_DAYS_REMINDED_TO_ADD_EXPENSE"
    const val REMINDED_TO_EXPORT = "REMINDED_TO_EXPORT"
    const val APP_PURCHASED = "APP_PURCHASED"
    const val ASKED_TO_REVIEW_APP = "ASKED_TO_REVIEW_APP"
    const val OPENED_GOOGLE_PLAY_FOR_REVIEW = "OPENED_GOOGLE_PLAY_FOR_REVIEW"
    const val PN_VIEWED = "PN_VIEWED"


    fun logEvent(eventName: String) {
        if (!BuildConfig.DEBUG) {
            Amplitude.getInstance().logEvent(eventName)
            FlurryAgent.logEvent(eventName)
        }
    }
}