package com.remotearthsolutions.expensetracker.activities.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils

class NotificationPermissionUtils(val context: MainActivity) {
    var hasNotificationPermissionGranted = false
    private var callBack: Runnable? = null

    private val notificationPermissionLauncher =
        context.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasNotificationPermissionGranted = isGranted
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (shouldShowRequestPermissionRationale(
                            context,
                            android.Manifest.permission.POST_NOTIFICATIONS
                        )
                    ) {
                        showNotificationPermissionRationale()
                    } else {
                        showSettingDialog()
                    }
                }
            } else {
                callBack?.run()
            }
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun startNotificationPermissionLauncher(callback: Runnable? = null) {
        this.callBack = callback
        notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun showSettingDialog() {

        AlertDialogUtils.show(
            context,
            "Notification Permission",
            "Notification permission is required, Please allow notification permission from setting",
            "Ok",
            "Cancel",
            null,
            object : BaseView.Callback {
                override fun onOkBtnPressed() {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:${context.packageName}")
                    context.startActivity(intent)
                }
            })
    }

    private fun showNotificationPermissionRationale() {
        AlertDialogUtils.show(
            context,
            "Alert",
            "Notification permission is required to show notification",
            "Ok",
            "Cancel",
            null,
            object : BaseView.Callback {
                override fun onOkBtnPressed() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        startNotificationPermissionLauncher()
                    }
                }
            })
    }

}
