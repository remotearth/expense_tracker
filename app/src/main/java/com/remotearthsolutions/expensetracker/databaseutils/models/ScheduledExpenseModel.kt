package com.remotearthsolutions.expensetracker.databaseutils.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "scheduled_expense")
class ScheduledExpenseModel {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @ColumnInfo(name = "period")
    var period: Int = 1
    @ColumnInfo(name = "periodtype")
    var periodtype: Int = 0
    @ColumnInfo(name = "occurrence")
    var occurrence: Int = 1
    @ColumnInfo(name = "nextoccurrencedate")
    var nextoccurrencedate: Long = 0
    @ColumnInfo(name = "categoryid")
    var categoryid: Int = 0
    @ColumnInfo(name = "accountid")
    var accountid: Int = 0
    @ColumnInfo(name = "amount")
    var amount: Double = 0.0
    @ColumnInfo(name = "note")
    var note: String? = ""

    @Ignore
    constructor()

    constructor(
        period: Int, periodtype: Int, occurrence: Int, nextoccurrencedate: Long,
        categoryid: Int, accountid: Int, amount: Double, note: String?
    ) {
        this.period = period
        this.periodtype = periodtype
        this.occurrence = occurrence
        this.nextoccurrencedate = nextoccurrencedate
        this.categoryid = categoryid
        this.accountid = accountid
        this.amount = amount
        this.note = note
    }
}