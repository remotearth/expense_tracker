package com.remotearthsolutions.expensetracker.databaseutils;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {CategoryModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{

    public  CategoryDao dao;
}