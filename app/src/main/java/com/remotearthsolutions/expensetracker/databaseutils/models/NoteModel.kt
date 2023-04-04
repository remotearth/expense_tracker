package com.remotearthsolutions.expensetracker.databaseutils.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
class NoteModel {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name = "note")
    var note: String? = null

    @Ignore
    constructor()

    constructor(note: String) {
        this.note = note
    }
}
