package com.remotearthsolutions.expensetracker.databaseutils;

import android.content.Context;
import androidx.room.Room;

public class DatabaseClient {


    private Context context;
    private AppDatabase appDatabase;
    private static DatabaseClient client;

    private DatabaseClient(Context context) {
        this.context = context;

        //creating the app database with Room database builder, here ExpenseTracker is the database name
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "ExpenseTracker").build();
    }


    public static synchronized DatabaseClient getInstance(Context context) {
        if (client == null) {
            client = new DatabaseClient(context);
        }
        return client;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
