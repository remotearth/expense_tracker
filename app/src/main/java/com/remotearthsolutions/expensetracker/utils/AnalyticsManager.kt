package com.remotearthsolutions.expensetracker.utils

import com.remotearthsolutions.expensetracker.BuildConfig
import com.remotearthsolutions.expensetracker.activities.ApplicationObject


object AnalyticsManager {
    const val APP_RESUMED = "app_resumed"
    const val EXPENSE_TYPE_DEFAULT = "expense_added"
    const val EXPENSE_TYPE_SCHEDULED = "expense_scheduled"
    const val EXPENSE_DELETED = "expense_deleted"
    const val EXPENSE_ACCOUNT_CREATED = "expense_account_created"
    const val EXPENSE_ACCOUNT_UPDATED = "expense_account_updated"
    const val EXPENSE_ACCOUNT_DELETED = "expense_account_deleted"
    const val EXPENSE_CATEGORY_CREATED = "expense_category_created"
    const val EXPENSE_CATEGORY_UPDATED = "expense_category_updated"
    const val EXPENSE_CATEGORY_DELETED = "expense_category_deleted"
    const val ACCOUNT_AMOUNT_TRANSFERRED = "account_amount_transferred"
    const val AD_SHOWN = "ad_shown"
    const val CLOUD_BACKUP = "cloud_backup"
    const val CLOUD_DOWNLOAD = "cloud_download"
    const val DATA_EXPORTED = "data_exported"
    const val EXPORT_REMINDER_ENABLED = "export_reminder_enabled"
    const val EXPORT_REMINDER_DISABLED = "export_reminder_disabled"
    const val ADD_EXPENSE_DAILY_REMINDER_ENABLED = "add_expense_daily_reminder_enabled"
    const val ADD_EXPENSE_DAILY_REMINDER_DISABLED = "add_expense_daily_reminder_disabled"
    const val AUTOMATIC_SALARY_ENABLED = "automatic_salary_enabled"
    const val AUTOMATIC_SALARY_DISABLED = "automatic_salary_disabled"
    const val REMINDED_TO_ADD_EXPENSE = "reminded_to_add_expense"
    const val AFTER_FIVE_DAYS_REMINDED_TO_ADD_EXPENSE = "after_five_days_reminded_to_add_expense"
    const val REMINDED_TO_EXPORT = "reminded_to_export"
    const val CLICKED_ON_PURCHASE_NAV_ITEM = "clicked_on_purchase_nav_item"
    const val INITIATE_PURCHASE = "initiate_purchase"
    const val ALREADY_PURCHASED = "already_purchased"
    const val ERROR_OCCURRED_ON_PURCHASE = "error_occurred_on_purchase"
    const val APP_PURCHASED = "app_purchased"
    const val USER_CANCELLED_APP_PURCHASE = "user_cancelled_app_purchase"
    const val ASKED_TO_REVIEW_APP = "asked_to_review_app"
    const val OPENED_GOOGLE_PLAY_FOR_REVIEW = "opened_google_play_for_review"
    const val PN_VIEWED = "pn_viewed"
    const val APP_SHARE_INTENT = "app_share_intent"
    const val NEW_USER = "new_user"
    const val ABOUT_PAGE_VIEWED = "about_page_viewed"
    const val PRIVACY_PAGE_VIEWED = "privacy_page_viewed"
    const val LICENSE_PAGE_VIEWED = "license_page_viewed"
    const val SETTINGS_PAGE_VIEWED = "settings_page_viewed"
    const val OPEN_ADD_EXPENSE_FROM_HOMEPAGE_FAB = "open_add_expense_from_homepage_fab"
    const val OPEN_ADD_EXPENSE_FROM_LIST_AT_HOMEPAGE = "open_add_expense_from_list_at_homepage"
    const val OPEN_ADD_EXPENSE_FROM_TRANSACTION_SCREEN = "open_add_expense_from_transaction_screen"


    fun logEvent(eventName: String, properties: Map<String, String> = emptyMap()) {
        if (!BuildConfig.DEBUG) {
            FirebaseUtils.logEvent(ApplicationObject.appInstance?.applicationContext, eventName, properties)
        }
    }
}