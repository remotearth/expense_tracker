package com.remotearthsolutions.expensetracker.databaseutils

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.utils.InitialDataGenerator.generateAccounts
import com.remotearthsolutions.expensetracker.utils.InitialDataGenerator.generateCategories
import java.util.concurrent.Executors

class DatabaseClient private constructor(private val context: Context) {
    val appDatabase: AppDatabase

    companion object {
        private var client: DatabaseClient? = null
        @Synchronized
        fun getInstance(context: Context): DatabaseClient? {
            if (client == null) {
                client = DatabaseClient(context)
            }
            return client
        }
    }

    init {

        val migrationFrom1to2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `scheduled_expense` (" +
                        "`id` INTEGER NOT NULL, `period` INTEGER NOT NULL, `periodtype` INTEGER NOT NULL, " +
                        "`occurrence` INTEGER NOT NULL, `nextoccurrencedate` INTEGER NOT NULL, `categoryid` INTEGER NOT NULL," +
                        "`accountid` INTEGER NOT NULL,`amount` REAL NOT NULL,`note` TEXT, PRIMARY KEY(`id`))")
            }
        }

        appDatabase = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            context.getString(R.string.database_name)
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Executors.newSingleThreadScheduledExecutor()
                        .execute {
                            val categoryDao =
                                getInstance(context)!!.appDatabase
                                    .categoryDao()
                            categoryDao.addAllCategories(
                                *generateCategories(context)
                            )
                            val accountDao =
                                getInstance(context)!!.appDatabase
                                    .accountDao()
                            accountDao.addAllAccounts(*generateAccounts(context))
                        }
                }

            })
            .addMigrations(migrationFrom1to2)
            .build()
    }
}