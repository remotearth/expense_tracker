package com.remotearthsolutions.expensetracker.databaseutils.models.dtos

import androidx.room.ColumnInfo

class ScheduledExpenseDto {
    @ColumnInfo(name = "id")
    var id: Long = 0

    @ColumnInfo(name = "period")
    var period: Int = 1

    @ColumnInfo(name = "periodtype")
    var periodtype: Int = 0

    @ColumnInfo(name = "occurrence")
    var occurrence: Int = 1

    @ColumnInfo(name = "nextoccurrencedate")
    var nextoccurrencedate: Long = 0

    @ColumnInfo(name = "amount")
    var amount: Double = 0.0

    @ColumnInfo(name = "note")
    var note: String? = ""

    @ColumnInfo(name = "categoryid")
    var categoryId: Int = 0

    @ColumnInfo(name = "category_name")
    var categoryName: String? = ""

    @ColumnInfo(name = "icon_name")
    var categoryIcon: String? = ""

    @ColumnInfo(name = "accountid")
    var accountId: Int = 0

    @ColumnInfo(name = "name")
    var accountName: String? = ""

    @ColumnInfo(name = "icon")
    var accountIcon: String? = ""
}
