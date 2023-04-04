package com.remotearthsolutions.expensetracker.databaseutils.daos

import androidx.room.*
import com.remotearthsolutions.expensetracker.databaseutils.models.NoteModel
import io.reactivex.Single

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(noteModel: NoteModel)

    @Delete
    fun delete(noteModel: NoteModel)

    @get:Query("Select * from notes")
    val allNotes: Single<List<NoteModel>>
}
