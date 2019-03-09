package com.remotearthsolutions.expensetracker.services;

import android.app.Activity;
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;

import java.util.List;

public interface FileProcessingService {
    void writeOnCsvFile(Activity activity, String content, Runnable runnable);
    List<CategoryExpense> readFromCsvFile(Activity activity);
    void loadTableData(String filepath, Callback callback);
    List<String> getNameOfAllCsvFile();
    void shareFile(Activity activity);

    interface Callback {
        void onComplete(List<CategoryModel> categories, List<ExpenseModel> expenseModels, List<AccountModel> accountModels);
    }
}
