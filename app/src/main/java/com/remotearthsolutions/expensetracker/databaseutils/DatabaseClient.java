package com.remotearthsolutions.expensetracker.databaseutils;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.utils.InitialDataGenerator;

import java.util.concurrent.Executors;

public class DatabaseClient {

    private Context context;
    private AppDatabase appDatabase;
    private static DatabaseClient client;

    private DatabaseClient(Context context) {
        this.context = context;
        //creating the app database with Room database builder, here ExpenseTracker is the database name
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, context.getString(R.string.database_name))
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);

                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                            CategoryDao categoryDao = getInstance(context).getAppDatabase().categoryDao();
                            categoryDao.addAllCategories(InitialDataGenerator.generateCategories(context));

                            AccountDao accountDao = getInstance(context).getAppDatabase().accountDao();
                            accountDao.addAllAccounts(InitialDataGenerator.generateAccounts(context));
                        });

                    }
                })
                .build();
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
