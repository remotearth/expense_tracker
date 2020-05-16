package com.remotearthsolutions.expensetracker.databaseutils.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.remotearthsolutions.expensetracker.databaseutils.models.WorkerIdModel
import io.reactivex.Single

@Dao
interface WorkerIdsDao {
    @Insert
    fun add(workerIdModel: WorkerIdModel)

    @Delete
    fun delete(workerIdModel: WorkerIdModel)

    @Query("Select * from workers_ids where scheduled_expense_id=:scheduledExpenseId")
    fun getWorkerIdModelByExpenseId(scheduledExpenseId: Long): Single<WorkerIdModel>
}