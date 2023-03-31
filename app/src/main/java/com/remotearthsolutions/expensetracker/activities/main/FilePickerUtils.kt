package com.remotearthsolutions.expensetracker.activities.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.contracts.BaseView

class FilePickerUtils {

    companion object {
        const val REQ_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 10
    }

    fun checkPermissionAndPickFile(mainActivity: MainActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            openFilePicker(mainActivity)
        } else {

            if (ActivityCompat.checkSelfPermission(
                    mainActivity, Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    mainActivity, arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), REQ_CODE_READ_EXTERNAL_STORAGE_PERMISSION
                )
            } else {
                openFilePicker(mainActivity)
            }
        }
    }

    fun openFilePicker(mainActivity: MainActivity) {
        with(mainActivity) {
            showAlert(resources.getString(R.string.attention),
                resources.getString(R.string.will_replace_your_current_entries),
                resources.getString(R.string.yes),
                resources.getString(R.string.cancel),
                null,
                object : BaseView.Callback {
                    override fun onOkBtnPressed() {

                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "text/*"
                        mainActivity.getResultListener().launch(intent)
                    }

                    override fun onCancelBtnPressed() {}
                })
        }
    }
}