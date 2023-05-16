package com.remotearthsolutions.expensetracker.fragments.settings

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.main.MainActivity
import com.remotearthsolutions.expensetracker.utils.AnalyticsManager
import com.remotearthsolutions.expensetracker.utils.LocalNotificationManager
import com.remotearthsolutions.expensetracker.utils.Utils

class ExportReminderWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        if (isStopped)
            return Result.failure()

        val intent = Intent(appContext, MainActivity::class.java)
        intent.putExtra("message", appContext.getString(R.string.safe_to_export))

        with(AnalyticsManager) { logEvent(REMINDED_TO_EXPORT) }
        LocalNotificationManager.showNotification(
            appContext,
            appContext.getString(R.string.app_name),
            appContext.getString(R.string.time_to_export_data),
            Utils.getPendingIntent(appContext, intent, 0)
        )
        ReminderWorkerHelper.setExportReminder(appContext)
        return Result.success()
    }
}
