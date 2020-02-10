package com.remotearthsolutions.expensetracker.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.PermissionListener
import com.remotearthsolutions.expensetracker.R

class PermissionUtils {
    fun writeExternalStoragePermission(
        activity: Activity?,
        permissionListener: PermissionListener?
    ) {
        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(permissionListener)
            .check()
    }

    fun readExternalStoragePermission(
        activity: Activity?,
        permissionListener: PermissionListener?
    ) {
        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(permissionListener)
            .check()
    }

    fun showSettingsDialog(activity: Activity) {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(activity.getString(R.string.storage_permission_needed_still_can_enable_from_settings))
        builder.setPositiveButton(
            activity.getString(R.string.go_to_settings)
        ) { dialog: DialogInterface, _: Int ->
            dialog.cancel()
            openSettings(activity)
        }
        builder.setNegativeButton(
            activity.getString(R.string.cancel)
        ) { dialog: DialogInterface, _: Int -> dialog.cancel() }
        builder.show()
    }

    private fun openSettings(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts(
            Constants.KEY_PACKAGE,
            activity.packageName,
            null
        )
        intent.data = uri
        activity.startActivityForResult(intent, 101)
    }
}