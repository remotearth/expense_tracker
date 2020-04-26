package com.remotearthsolutions.expensetracker.databaseutils.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.remotearthsolutions.expensetracker.databaseutils.models.ScheduledExpenseModel
import io.reactivex.Flowable

@Dao
interface ScheduledExpenseDao {
    @Insert
    fun add(scheduledExpenseModel: ScheduledExpenseModel)

    @Delete
    fun delete(scheduledExpenseModel: ScheduledExpenseModel)

    @Query("Select * from scheduled_expense")
    fun getAllScheduledExpenses(): Flowable<List<ScheduledExpenseModel>>
}