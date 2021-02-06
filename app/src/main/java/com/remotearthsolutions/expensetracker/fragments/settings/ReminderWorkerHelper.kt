package com.remotearthsolutions.expensetracker.fragments.settings

import android.content.Context
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import com.remotearthsolutions.expensetracker.utils.workmanager.WorkManagerEnqueuer
import com.remotearthsolutions.expensetracker.utils.workmanager.WorkManagerHelper
import com.remotearthsolutions.expensetracker.utils.workmanager.WorkRequestType
import java.util.*

object ReminderWorkerHelper {

    fun setExportReminder(context: Context) {
        val delay = getNextExportReminderDelayMillis()
        val requestId = WorkManagerEnqueuer()
            .enqueue<ExportReminderWorker>(
                context,
                WorkRequestType.ONETIME,
                delay,
                null
            )
        SharedPreferenceUtils.getInstance(context)
            ?.putString(Constants.KEY_EXPORT_REMINDER_WORKREQUEST_ID, requestId)
    }

    private fun getNextExportReminderDelayMillis(): Long {
        val cal = Calendar.getInstance()
        val currentMillis = cal.timeInMillis
        cal.add(Calendar.MONTH, 1)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 1)
        cal.set(Calendar.SECOND, 0)
        return cal.timeInMillis - currentMillis
    }

    fun getCountOfExpenseAddedInLastFourHoursSync(context: Context): Int {
        val db = DatabaseClient.getInstance(context).appDatabase
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 22)
        cal.set(Calendar.MINUTE, 0)
        val endTime = cal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY, 19)
        cal.set(Calendar.MINUTE, 0)
        val startTime = cal.timeInMillis
        return db.expenseDao().getNumberOfExpenseEntryWithinRange(startTime, endTime).blockingGet()
    }

    fun setAddExpenseReminder(context: Context) {
        val requestId = WorkManagerEnqueuer()
            .enqueue<AddExpenseReminderWorker>(
                context,
                WorkRequestType.ONETIME,
                getNextAddExpenseReminderDelay(),
                null
            )
        SharedPreferenceUtils.getInstance(context)
            ?.putString(Constants.KEY_ADDEXPENSE_REMINDER_WORKREQUEST_ID, requestId)
    }

    private fun getNextAddExpenseReminderDelay(): Long {
        val cal = Calendar.getInstance()
        val currentTimeMillis = cal.timeInMillis
        cal.add(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 22)
        cal.set(Calendar.MINUTE, 0)
        return cal.timeInMillis - currentTimeMillis
    }

    fun cancelReminder(context: Context, key: String) {
        val requestId = SharedPreferenceUtils.getInstance(context)
            ?.getString(key, "")
        (requestId?.length!! > 0).let {
            WorkManagerHelper.cancelWorkRequest(context, requestId)
        }
    }
}