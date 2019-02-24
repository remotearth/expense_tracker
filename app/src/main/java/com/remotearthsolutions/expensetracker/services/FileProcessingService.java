package com.remotearthsolutions.expensetracker.services;

import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import java.util.List;

public interface FileProcessingService {

    void createCsvFile();
    void writeOnCsvFile(CategoryExpense categoryExpense);
    List<CategoryExpense> readFromCsvFile();

}
