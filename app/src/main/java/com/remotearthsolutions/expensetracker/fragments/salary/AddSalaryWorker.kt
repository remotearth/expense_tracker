package com.remotearthsolutions.expensetracker.fragments.salary

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.main.MainActivity
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.LocalNotificationManager
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import java.util.*


class AddSalaryWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        if (isStopped)
            return Result.failure()

        val sharedPreferenceUtils = SharedPreferenceUtils.getInstance(appContext)
        val amount =
            sharedPreferenceUtils?.getString(Constants.KEY_SALARY_AUTOMATIC_AMOUNT, "0")?.toDouble()
        val accountId = sharedPreferenceUtils?.getInt(Constants.KEY_SALARY_AUTOMATIC_ACCOUNT_ID, 1)
        
        val accountDao = DatabaseClient.getInstance(appContext).appDatabase.accountDao()
        val accountModel = accountDao.getAccountById(accountId!!).blockingGet()
        accountModel.amount += amount!!
        accountDao.addAccount(accountModel)

        val salaryDate = sharedPreferenceUtils.getLong(
            Constants.KEY_SALARY_AUTOMATIC_DATE,
            0
        )
        val cal = Calendar.getInstance()
        if (salaryDate > 0) {
            cal.timeInMillis = salaryDate
        }
        cal.add(Calendar.MONTH, 1)
        sharedPreferenceUtils.putLong(Constants.KEY_SALARY_AUTOMATIC_DATE, cal.timeInMillis)

        SalaryWorkerHelper.setAutomaticSalary(appContext, cal.timeInMillis)

        val intent = Intent(appContext, MainActivity::class.java)
        intent.putExtra("message", appContext.getString(R.string.salary_is_added))
        val pendingIntent =
            PendingIntent.getActivity(appContext, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        LocalNotificationManager.showNotification(
            appContext,
            appContext.getString(R.string.app_name),
            appContext.getString(R.string.salary_is_added),
            pendingIntent
        )

        return Result.success()
    }
}
