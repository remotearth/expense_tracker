package com.remotearthsolutions.expensetracker.fragments.salary

import android.content.Context
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import com.remotearthsolutions.expensetracker.utils.workmanager.WorkManagerEnqueuer
import com.remotearthsolutions.expensetracker.utils.workmanager.WorkManagerHelper
import com.remotearthsolutions.expensetracker.utils.workmanager.WorkRequestType
import java.util.*


object SalaryWorkerHelper {

    fun setAutomaticSalary(context: Context, nextSalaryDate: Long) {
        val delay = nextSalaryDate - Calendar.getInstance().timeInMillis
        val requestId = WorkManagerEnqueuer()
            .enqueue<AddSalaryWorker>(
                context,
                WorkRequestType.ONETIME,
                delay,
                null
            )
        SharedPreferenceUtils.getInstance(context)
            ?.putString(Constants.KEY_SALARY_AUTOMATIC_WORKER_ID, requestId)
    }

    fun cancelAutomaticSalary(context: Context) {
        val requestId = SharedPreferenceUtils.getInstance(context)
            ?.getString(Constants.KEY_SALARY_AUTOMATIC_WORKER_ID, "")
        (requestId?.length!! > 0).let {
            WorkManagerHelper.cancelWorkRequest(context, requestId)
        }
    }
}