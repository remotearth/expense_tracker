package com.remotearthsolutions.expensetracker

import android.content.Context

class Configs {

    companion object {
        fun getAppCenterAppSecret(context: Context): String {
            return if (BuildConfig.DEBUG) {
                context.getString(R.string.appcenter_app_secret_dev)
            } else {
                context.getString(R.string.appcenter_app_secret)
            }
        }
    }


}