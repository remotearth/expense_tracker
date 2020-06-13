package com.remotearthsolutions.expensetracker.utils

import android.app.Activity
import android.content.Context
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.remotearthsolutions.expensetracker.services.InternetCheckerServiceImpl


object RemoteConfigManager {
    private const val EXPENSE_COUNT_NEEDED_FOR_AUTO_BACKUP = "EXPENSE_COUNT_NEEDED_FOR_AUTO_BACKUP"

    private val remoteConfig = Firebase.remoteConfig
    private val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 3 * 24 * 3600 // 3 days
    }

    init {
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    fun update(context: Context) {
        if (!InternetCheckerServiceImpl(context).isConnected) {
            return
        }
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    val expenseCount =
                        remoteConfig.getString(EXPENSE_COUNT_NEEDED_FOR_AUTO_BACKUP).toInt()
                    SharedPreferenceUtils.getInstance(context)
                        ?.putInt(Constants.KEY_EXPENSE_COUNT_AUTO_BACKUP_DELAY, expenseCount)
                }
            }.addOnFailureListener {
                it.printStackTrace()
            }
    }
}