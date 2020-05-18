package com.remotearthsolutions.expensetracker.databaseutils.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.remotearthsolutions.expensetracker.databaseutils.models.ScheduledExpenseModel
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.ScheduledExpenseDto
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface ScheduledExpenseDao {
    @Insert
    fun add(scheduledExpenseModel: ScheduledExpenseModel): Single<Long>

    @Update
    fun update(scheduledExpenseModel: ScheduledExpenseModel)

    @Query("Delete from scheduled_expense Where id = :id")
    fun delete(id: Long)

    @Query("Select * from scheduled_expense")
    fun getAllScheduledExpenses(): Flowable<List<ScheduledExpenseModel>>

    @Query(
        "SELECT scheduled_expense.id, scheduled_expense.period, scheduled_expense.periodtype," +
                "scheduled_expense.occurrence,scheduled_expense.nextoccurrencedate,scheduled_expense.categoryid," +
                "category.category_name,category.icon_name,scheduled_expense.accountid,account.name," +
                "account.icon,scheduled_expense.amount,scheduled_expense.note FROM ((scheduled_expense " +
                "INNER JOIN account ON scheduled_expense.accountid = account.id) " +
                "INNER JOIN category ON scheduled_expense.categoryid = category.id)"
    )
    fun getScheduledExpensesWithCategoryAndAccount(): Flowable<List<ScheduledExpenseDto>>

    @Query(
        "SELECT scheduled_expense.id, scheduled_expense.period, scheduled_expense.periodtype," +
                "scheduled_expense.occurrence,scheduled_expense.nextoccurrencedate,scheduled_expense.categoryid," +
                "category.category_name,category.icon_name,scheduled_expense.accountid,account.name," +
                "account.icon,scheduled_expense.amount,scheduled_expense.note FROM ((scheduled_expense " +
                "INNER JOIN account ON scheduled_expense.accountid = account.id) " +
                "INNER JOIN category ON scheduled_expense.categoryid = category.id) WHERE scheduled_expense.id = :scheduledExpenseId"
    )
    fun getScheduledExpensesWithCategoryAndAccountById(scheduledExpenseId: Long): Single<ScheduledExpenseDto>
}
