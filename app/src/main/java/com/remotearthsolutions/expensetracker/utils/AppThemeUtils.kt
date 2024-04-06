package com.remotearthsolutions.expensetracker.utils

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

class AppThemeUtils {

    companion object {

        fun setAppTheme(context: Context, isDarkModeEnabled: Boolean) {
            if (isDarkModeEnabled) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val uiModeManager =
                        context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
                    uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val uiModeManager =
                        context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
                    uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_NO)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }


        }
    }
}