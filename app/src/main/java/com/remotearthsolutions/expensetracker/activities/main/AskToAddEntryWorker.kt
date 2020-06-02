package com.remotearthsolutions.expensetracker.activities.main

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.utils.AnalyticsManager
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.LocalNotificationManager
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import java.util.*


class AskToAddEntryWorker(private val appContext: Context, workerParameters: WorkerParameters) :
    Worker(appContext, workerParameters) {
    override fun doWork(): Result {
        if (isStopped)
            return Result.failure()

        val sharedPreferenceUtils = SharedPreferenceUtils.getInstance(appContext)
        val delay = sharedPreferenceUtils?.getLong(
            Constants.KEY_PERIODIC_ADD_EXPENSE_REMINDER,
            Constants.DELAY_PERIODIC_REMINDER_TO_ADD_EXPENSE
        )
        val cal = Calendar.getInstance()
        val startDate = cal.timeInMillis - delay!!
        val expenseDao = DatabaseClient.getInstance(appContext).appDatabase.expenseDao()
        val count =
            expenseDao.getNumberOfExpenseEntryWithinRange(startDate, cal.timeInMillis).blockingGet()
        val isDailyReminderOn =
            sharedPreferenceUtils.getBoolean(Constants.PREF_REMIND_TO_ADDEXPENSE, false)

        if (count == 0 && !isDailyReminderOn) {
            val intent = Intent(appContext, MainActivity::class.java)
            intent.putExtra(
                "message",
                appContext.getString(R.string.periodic_ask_to_add_expense)
            )
            val pendingIntent =
                PendingIntent.getActivity(appContext, 0, intent, PendingIntent.FLAG_ONE_SHOT)

            with(AnalyticsManager) {
                logEvent(AFTER_FIVE_DAYS_REMINDED_TO_ADD_EXPENSE)
            }
            LocalNotificationManager.showNotification(
                appContext,
                appContext.getString(R.string.app_name),
                appContext.getString(R.string.periodic_remind_notification),
                pendingIntent
            )
        }

        return Result.success()
    }
}