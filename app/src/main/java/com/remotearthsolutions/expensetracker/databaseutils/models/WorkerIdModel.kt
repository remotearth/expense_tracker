package com.remotearthsolutions.expensetracker.databaseutils.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "workers_ids")
class WorkerIdModel {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "scheduled_expense_id")
    var scheduledExpenseId: Long = 1

    @ColumnInfo(name = "worker_id")
    var workerId: String = ""

    @Ignore
    constructor()

    constructor(scheduledExpenseId: Long, workerId: String) {
        this.scheduledExpenseId = scheduledExpenseId
        this.workerId = workerId
    }
}