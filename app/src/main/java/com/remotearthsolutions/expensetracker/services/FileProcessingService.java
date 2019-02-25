package com.remotearthsolutions.expensetracker.services;

import android.app.Activity;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import java.util.List;

public interface FileProcessingService {

    void createCsvFile();
    void writeOnCsvFile(Activity activity, CategoryExpense categoryExpense);
    List<CategoryExpense> readFromCsvFile();

}
