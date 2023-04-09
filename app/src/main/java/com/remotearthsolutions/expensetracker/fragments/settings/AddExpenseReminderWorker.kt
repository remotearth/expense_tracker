package com.remotearthsolutions.expensetracker.fragments.settings

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.main.MainActivity
import com.remotearthsolutions.expensetracker.utils.AnalyticsManager
import com.remotearthsolutions.expensetracker.utils.LocalNotificationManager
import kotlin.random.Random

class AddExpenseReminderWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        if (isStopped)
            return Result.failure()

        val messages = appContext.resources.getStringArray(R.array.daily_reminder_messages)
        val message = messages[Random.nextInt(messages.size)]

        val intent = Intent(appContext, MainActivity::class.java)
        intent.putExtra("message", message)

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(appContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(appContext, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        }

        val count = ReminderWorkerHelper.getCountOfExpenseAddedInLastFourHoursSync(appContext)
        if (count == 0) {
            with(AnalyticsManager) { logEvent(REMINDED_TO_ADD_EXPENSE) }
            LocalNotificationManager.showNotification(
                appContext,
                appContext.getString(R.string.app_name),
                message,
                pendingIntent
            )
        }

        ReminderWorkerHelper.setAddExpenseReminder(appContext)
        return Result.success()
    }
}