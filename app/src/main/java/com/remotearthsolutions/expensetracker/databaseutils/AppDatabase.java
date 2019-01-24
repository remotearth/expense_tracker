package com.remotearthsolutions.expensetracker.databaseutils;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.IncomeModel;


@Database(entities = {CategoryModel.class, AccountModel.class, IncomeModel.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase {
    public abstract CategoryDao categoryDao();
    public abstract AccountDao accountDao();
}