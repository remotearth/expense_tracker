package com.remotearthsolutions.expensetracker.fragments.addexpensescreen

import android.content.Context
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel
import com.remotearthsolutions.expensetracker.databaseutils.models.ScheduledExpenseModel
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils
import java.util.*


class AddScheduledExpenseWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {
    override fun doWork(): Result {

        val scheduledExpenseId = inputData.getLong(ExpenseScheduler.SCHEDULED_EXPENSE_ID, -1)
        val db = DatabaseClient.getInstance(appContext).appDatabase
        val scheduledExpense = db.scheduleExpenseDao()
            .getScheduledExpensesWithCategoryAndAccountById(scheduledExpenseId).blockingGet()

        val workerIdModel =
            db.workerIdDao().getWorkerIdModelByExpenseId(scheduledExpenseId).blockingGet()
        db.workerIdDao().delete(workerIdModel)

        with(scheduledExpense) {
            db.expenseDao().add(
                ExpenseModel(
                    categoryId, accountId, amount,
                    DateTimeUtils.getCurrentTimeInMills(), note
                )
            )
            val account = db.accountDao().getAccountById(accountId).blockingGet()
            account.amount -= amount
            db.accountDao().updateAccount(account)

            if (occurrence > 1) {
                nextoccurrencedate =
                    ExpenseScheduler.nextOcurrenceDate(
                        nextoccurrencedate,
                        period,
                        periodtype
                    )
                occurrence--
                db.scheduleExpenseDao().update(ScheduledExpenseModel().fromDto(this))
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