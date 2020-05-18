package com.remotearthsolutions.expensetracker.fragments.settings

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.utils.LocalNotificationManager

class ExportReminderWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        LocalNotificationManager.showNotification(
            appContext,
            appContext.getString(R.string.app_name),
            appContext.getString(R.string.time_to_export_data),
            null
        )
        ExportReminderHelper.setReminderToExport(appContext)
        return Result.success()
    }


}