package com.remotearthsolutions.expensetracker.utils

interface Constants {
    companion object {
        const val KEY_SELECTED_ACCOUNT_ID = "KEY_SELECTED_ACCOUNT_ID"
        const val KEY_USER = "KEY_USER"
        const val KEY_URL = "KEY"

        const val URL_HOW_TO_USE =
            "https://sites.google.com/view/expense-spending-tracker/how-to-use"
        const val URL_PRIVACY_POLICY =
            "https://sites.google.com/view/expense-spending-tracker/privacy"
        const val URL_THIRD_PARTY_LICENSES =
            "https://sites.google.com/view/expense-spending-tracker/licenses"

        const val KEY_DAILY = "Daily"
        const val KEY_WEEKLY = "Weekly"
        const val KEY_MONTHLY = "Monthly"
        const val KEY_YEARLY = "Yearly"
        const val CATEGORYEXPENSE_PARCEL = "categoryexpense_parcel"

        const val LICENSE_KEY =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjdEZ6Ng4jqYE4XL2Zn6aSCfqnm5EzLTVlFPFTuXrAt3aTOxTLQBsdUn2Yu7FUCCJc27Ao/v9loyEzlEFOly90Ye1eVFzfnc7dNHVkHMHSYLp3k2XcIcN0xIbHgncCPTc/8i1pAxLHVzCPOLKe7GFp27HrQFPrz46jz2lG7RYRIFcfGlLL6nX8NAZWV5tpD2OVU3h5u5xDWIAL65O0vCaR8adRDbAClairUxn3zOsYQ5BJHs2cWOUR0qUeMcHjsYKoIMbWbWvd0JGOEJ8m/tMzmePz8hhXTS+/lZxY1ezelq8zlbnSQ3E3bA7lRNHZLuCFHx7X82qTAugMHXCr7pqVQIDAQAB"
        const val TEST_PURCHASED_ITEM = "android.test.purchased"
        const val PRODUCT_ID = "com.remotearthsolutions.expensetracker.remove_ad"


        const val PREF_CURRENCY = "pref_currency_list"
        const val PREF_PERIOD = "pref_period_list"
        const val PREF_LANGUAGE = "pref_language"
        const val PREF_TIME_FORMAT = "pref_time_format_list"
        const val PREF_REMIND_TO_ADDEXPENSE = "pref_remind_add_expense"
        const val PREF_REMIND_TO_EXPORT = "pref_remind_to_export"
        const val PREF_AUTO_DATABACKUP = "pref_auto_databackup"
        const val PREF_ISFIRSTTIMEVISITED = "PREF_ISFIRSTTIMEVISITED"

        const val KEY_SALARY_AUTOMATIC = "KEY_SALARY_AUTOMATIC"
        const val KEY_SALARY_AUTOMATIC_AMOUNT = "KEY_SALARY_AUTOMATIC_AMOUNT"
        const val KEY_SALARY_AUTOMATIC_DATE = "KEY_SALARY_AUTOMATIC_DATE"
        const val KEY_SALARY_AUTOMATIC_ACCOUNT_ID = "KEY_SALARY_AUTOMATIC_ACCOUNT_ID"
        const val KEY_SALARY_AUTOMATIC_WORKER_ID = "KEY_SALARY_AUTOMATIC_WORKER_ID"
        const val KEY_EXPENSE_COUNT_AUTO_BACKUP = "KEY_EXPENSE_COUNT_AUTO_BACKUP"
        const val KEY_EXPENSE_COUNT_NEEDED_FOR_AUTO_BACKUP = "expense_count_needed_for_auto_backup"
        const val KEY_SHOW_HOW_TO_USE_NAV_MENU = "show_how_to_use_nav_menu"

        const val KEY_TITLE = "title"
        const val KEY_SCREEN = "screen"
        const val KEY_UTF_VERSION = "UTF-8"
        const val KEY_META1_REPLACE = "meta1:"
        const val KEY_META2_REPLACE = "meta2:"
        const val KEY_META3_REPLACE = "meta3:"
        const val KEY_EXPENSE_TRACKER = "expense_tracker_"
        const val KEY_CSV_FILE_EXTENSION = ".csv"
        const val KEY_GOOGLE_CLIENT_ID =
            "1034332655323-lv5t5fja6ef5co4vsaf9bv476c9rga9r.apps.googleusercontent.com"
        const val KEY_MESSAGE = "message"
        const val KEY_VERSION_CODE = "version_code"
        const val KEY_DATE_MONTH_YEAR_DEFAULT = "dd-MM-yyyy"
        const val KEY_DATE_MONTH_YEAR_BACKSLASH = "dd/MM/yyyy"
        const val KEY_MONTH_DATE_YEAR_BACKSLASH = "MM/dd/yyyy"
        const val KEY_YEAR_MONTH_DATE_BACKSLASH = "yyyy/mm/dd"
        const val KEY_DATE_MONTH_YEAR_HOUR_MINIT_AM_PM = "dd-MM-yyyy h:mm a"
        const val KEY_DATE_MONTH_YEAR_HOUR_MIN_SEC = "dd-MM-yyyy h:mm:ss"
        const val KEY_MONTH_YEAR = "MMMM, yyyy"
        const val KEY_YEAR = "yyyy"
        const val KEY_MONTH = "MMMM"
        const val KEY_HOUR_MIN_SEC = "h:mm:ss"
        const val KEY_PACKAGE = "package"
        const val KEY_COLOR_FORMAT = "#%02x%02x%02x"
        const val KEY_EXPORT_REMINDER_WORKREQUEST_ID = "KEY_EXPORT_REMINDER_WORKREQUEST_ID"
        const val KEY_ADDEXPENSE_REMINDER_WORKREQUEST_ID = "KEY_ADDEXPENSE_REMINDER_WORKREQUEST_ID"

        const val KEY_PERIODIC_ADD_EXPENSE_REMINDER = "KEY_PERIODIC_ADD_EXPENSE_REMINDER"
        const val KEY_PERIODIC_ADD_EXPENSE_REMINDER_WORKER_ID =
            "KEY_PERIODIC_ADD_EXPENSE_REMINDER_WORKER_ID"

        const val USER_TOLD_NEVER_ASK_TO_REVIEW = "USER_TOLD_NEVER_ASK_TO_REVIEW"
        const val ENTRY_NEEDED = "ENTRY_NEEDED"
        const val DEFAULT_NUMBER_OF_ENTRY_NEEDED = 50

        const val DONOT_EDIT_META_DATA =
            "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                    "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                    "(Don\'t edit this meta data)\n\n"

        const val DELAY_PERIODIC_REMINDER_TO_ADD_EXPENSE: Long = 3 * 24 * 3600 * 1000  // 3 days

    }
}