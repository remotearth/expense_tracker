package com.remotearthsolutions.expensetracker.databaseutils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.remotearthsolutions.expensetracker.databaseutils.daos.*
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel
import com.remotearthsolutions.expensetracker.databaseutils.models.ScheduledExpenseModel

@Database(
    entities = [CategoryModel::class, AccountModel::class,
        ExpenseModel::class,ScheduledExpenseModel::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun accountDao(): AccountDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryExpenseDao(): CategoryExpenseDao
    abstract fun scheduleExpenseDao(): ScheduledExpenseDao
}