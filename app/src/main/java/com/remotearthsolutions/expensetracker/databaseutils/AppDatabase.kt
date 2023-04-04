package com.remotearthsolutions.expensetracker.databaseutils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.remotearthsolutions.expensetracker.databaseutils.daos.*
import com.remotearthsolutions.expensetracker.databaseutils.models.*

@Database(
    entities = [CategoryModel::class, AccountModel::class,
        ExpenseModel::class, ScheduledExpenseModel::class, WorkerIdModel::class, NoteModel::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun accountDao(): AccountDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryExpenseDao(): CategoryExpenseDao
    abstract fun scheduleExpenseDao(): ScheduledExpenseDao
    abstract fun workerIdDao(): WorkerIdsDao
    abstract fun notesDao(): NotesDao
}
