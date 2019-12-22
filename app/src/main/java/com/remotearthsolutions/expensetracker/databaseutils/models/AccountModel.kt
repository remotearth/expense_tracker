package com.remotearthsolutions.expensetracker.databaseutils.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "account")
class AccountModel {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    @ColumnInfo(name = "name")
    var name: String? = null
    @ColumnInfo(name = "icon")
    var icon: String? = null
    @ColumnInfo(name = "amount")
    var amount = 0.0
    @ColumnInfo(name = "notremovable")
    var notremovable = 0

    @Ignore
    constructor()

    constructor(
        name: String?,
        icon: String?,
        amount: Double,
        notremovable: Int
    ) {
        this.name = name
        this.icon = icon
        this.amount = amount
        this.notremovable = notremovable
    }

}