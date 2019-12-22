package com.remotearthsolutions.expensetracker.databaseutils.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.parceler.Parcel

@Parcel
@Entity(tableName = "category")
class CategoryModel {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    @ColumnInfo(name = "category_name")
    var name: String? = null
    @ColumnInfo(name = "icon_name")
    var icon: String? = null
    @ColumnInfo(name = "notremovable")
    var notremovable = 0

    @Ignore
    constructor() {
    }

    constructor(name: String?, icon: String?, notremovable: Int) {
        this.name = name
        this.icon = icon
        this.notremovable = notremovable
    }

}