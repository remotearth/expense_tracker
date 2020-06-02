package com.remotearthsolutions.expensetracker.fragments.settings

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.main.MainActivity
import com.remotearthsolutions.expensetracker.utils.AnalyticsManager
import com.remotearthsolutions.expensetracker.utils.LocalNotificationManager

class ExportReminderWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        if (isStopped)
            return Result.failure()

        val intent = Intent(appContext, MainActivity::class.java)
        intent.putExtra("message", appContext.getString(R.string.safe_to_export))
        val pendingIntent =
            PendingIntent.getActivity(appContext, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        with(AnalyticsManager) { logEvent(REMINDED_TO_EXPORT) }
        LocalNotificationManager.showNotification(
            appContext,
            appContext.getString(R.string.app_name),
            appContext.getString(R.string.time_to_export_data),
            pendingIntent
        )
        ReminderWorkerHelper.setExportReminder(appContext)
        return Result.success()
    }
}
