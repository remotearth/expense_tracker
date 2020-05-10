package com.remotearthsolutions.expensetracker.databaseutils.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.remotearthsolutions.expensetracker.databaseutils.models.ScheduledExpenseModel
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.ScheduledExpenseDto
import io.reactivex.Flowable

@Dao
interface ScheduledExpenseDao {
    @Insert
    fun add(scheduledExpenseModel: ScheduledExpenseModel)

    @Delete
    fun delete(scheduledExpenseModel: ScheduledExpenseModel)

    @Query("Select * from scheduled_expense")
    fun getAllScheduledExpenses(): Flowable<List<ScheduledExpenseModel>>

    @Query("SELECT scheduled_expense.id, scheduled_expense.period, scheduled_expense.periodtype," +
            "scheduled_expense.occurrence,scheduled_expense.nextoccurrencedate,scheduled_expense.categoryid," +
            "category.category_name,category.icon_name,scheduled_expense.accountid,account.name," +
            "account.icon,scheduled_expense.amount,scheduled_expense.note FROM ((scheduled_expense " +
            "INNER JOIN account ON scheduled_expense.accountid = account.id) " +
            "INNER JOIN category ON scheduled_expense.categoryid = category.id)")
    fun getScheduledExpensesWithCategoryAndAccount(): Flowable<List<ScheduledExpenseDto>>
}
