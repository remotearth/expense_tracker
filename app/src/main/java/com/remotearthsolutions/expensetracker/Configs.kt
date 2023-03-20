package com.remotearthsolutions.expensetracker

import android.content.Context
import com.remotearthsolutions.expensetracker.utils.Constants

class Configs {

    companion object {
        fun getAppCenterAppSecret(context: Context): String {
            return if (BuildConfig.DEBUG) {
                context.getString(R.string.appcenter_app_secret_dev)
            } else {
                context.getString(R.string.appcenter_app_secret)
            }
        }

        fun getAdFreeProductId(): String {
            return if (BuildConfig.DEBUG) {
                Constants.TEST_PURCHASED_ITEM
            } else {
                Constants.PRODUCT_ID
            }
        }
    }

}