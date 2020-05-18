package com.remotearthsolutions.expensetracker.fragments.settings

import android.content.Context
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import com.remotearthsolutions.expensetracker.utils.workmanager.WorkManagerEnqueuer
import com.remotearthsolutions.expensetracker.utils.workmanager.WorkManagerHelper
import com.remotearthsolutions.expensetracker.utils.workmanager.WorkRequestType
import java.util.*


object ExportReminderHelper {

    fun setReminderToExport(context: Context) {
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

    fun cancelExportReminder(context: Context) {
        val requestId = SharedPreferenceUtils.getInstance(context)
            ?.getString(Constants.KEY_EXPORT_REMINDER_WORKREQUEST_ID, "")
        (requestId?.length!! > 0).let {
            WorkManagerHelper.cancelWorkRequest(context, requestId)
        }
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
}