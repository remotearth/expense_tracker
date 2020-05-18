package com.remotearthsolutions.expensetracker.fragments.addexpensescreen

import android.content.Context
import androidx.work.Data
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel
import com.remotearthsolutions.expensetracker.databaseutils.models.ScheduledExpenseModel
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils
import com.remotearthsolutions.expensetracker.utils.workmanager.WorkManagerEnqueuer
import com.remotearthsolutions.expensetracker.utils.workmanager.WorkRequestType
import java.util.*


class AddScheduledExpenseWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        if (isStopped)
            return Result.failure()

        val db = DatabaseClient.getInstance(appContext).appDatabase
        val scheduledExpenseId = inputData.getLong(ExpenseScheduler.SCHEDULED_EXPENSE_ID, -1)
        val scheduledExpense = db.scheduleExpenseDao()
            .getScheduledExpensesWithCategoryAndAccountById(scheduledExpenseId).blockingGet()

        with(scheduledExpense) {
            //add expense
            db.expenseDao().add(
                ExpenseModel(
                    categoryId, accountId, amount,
                    DateTimeUtils.getCurrentTimeInMills(), note
                )
            )
            //update account
            val account = db.accountDao().getAccountById(accountId).blockingGet()
            account.amount -= amount
            db.accountDao().updateAccount(account)

            val workerIdModel =
                db.workerIdDao().getWorkerIdModelByExpenseId(id).blockingGet()

            if (occurrence > 1) {
                // if expense needs to be repeated more
                //update next occurrence date
                nextoccurrencedate =
                    ExpenseScheduler.nextOcurrenceDate(
                        nextoccurrencedate,
                        period,
                        periodtype
                    )
                //decrease repeat count by 1
                occurrence--
                //upate ScheduledExpenseModel with new nextoccurrencedate and occurrence value
                db.scheduleExpenseDao().update(ScheduledExpenseModel().fromDto(this))

                //set another onetime request to workmanager for next occurrence
                val delay = nextoccurrencedate - Calendar.getInstance().timeInMillis
                val data = Data.Builder()
                data.putLong(ExpenseScheduler.SCHEDULED_EXPENSE_ID, id)
                val workRequestId = WorkManagerEnqueuer()
                    .enqueue<AddScheduledExpenseWorker>(
                        appContext,
                        WorkRequestType.ONETIME,
                        delay,
                        data.build()
                    )
                //update workrequest for the scheduled expense
                workerIdModel.workerId = workRequestId
                db.workerIdDao().update(workerIdModel)
            } else {
                db.scheduleExpenseDao().delete(id)
                WorkManager.getInstance(appContext)
                    .cancelWorkById(UUID.fromString(workerIdModel.workerId))
                db.workerIdDao().delete(workerIdModel)
            }
        }

        return Result.success()
    }
}