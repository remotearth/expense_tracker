package com.remotearthsolutions.expensetracker.databaseutils.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "expense")
class ExpenseModel {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    @ColumnInfo(name = "category_id")
    var categoryId = 0
    @ColumnInfo(name = "account_id")
    var source = 0
    @ColumnInfo(name = "amount")
    var amount = 0.0
    @ColumnInfo(name = "datetime")
    var datetime: Long = 0
    @ColumnInfo(name = "note")
    var note: String? = null

    constructor(
        categoryId: Int,
        source: Int,
        amount: Double,
        datetime: Long,
        note: String?
    ) {
        this.categoryId = categoryId
        this.source = source
        this.amount = amount
        this.datetime = datetime
        this.note = note
    }

    @Ignore
    constructor()

}