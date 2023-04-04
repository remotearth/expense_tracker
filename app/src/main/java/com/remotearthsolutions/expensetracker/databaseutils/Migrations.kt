package com.remotearthsolutions.expensetracker.databaseutils

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migrations {
    companion object {
        val migrationFrom1to2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `scheduled_expense` (" +
                            "`id` INTEGER NOT NULL, `period` INTEGER NOT NULL, `periodtype` INTEGER NOT NULL, " +
                            "`occurrence` INTEGER NOT NULL, `nextoccurrencedate` INTEGER NOT NULL, `categoryid` INTEGER NOT NULL," +
                            "`accountid` INTEGER NOT NULL,`amount` REAL NOT NULL,`note` TEXT, PRIMARY KEY(`id`))"
                )
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `workers_ids` (" +
                            "`id` INTEGER NOT NULL, `scheduled_expense_id` INTEGER NOT NULL, `worker_id` TEXT NOT NULL, PRIMARY KEY(`id`))"
                )
            }
        }

        val migrationFrom2to3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `notes` (`id` INTEGER NOT NULL, `note` TEXT NOT NULL, PRIMARY KEY(`id`))"
                )
            }
        }
    }
}