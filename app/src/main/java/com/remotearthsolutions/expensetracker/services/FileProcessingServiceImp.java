package com.remotearthsolutions.expensetracker.services;

import android.os.Environment;
import android.util.Log;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;

import java.io.*;
import java.util.List;

public class FileProcessingServiceImp implements FileProcessingService {

    private static final String FILE_NAME = "expense.csv";
    private File directory;

    public FileProcessingServiceImp() {
        directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + FILE_NAME);
    }

    @Override
    public void createCsvFile() {

        if (!directory.exists()) {
            try {
                directory.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void writeOnCsvFile(CategoryExpense categoryExpense) {
        if (!directory.exists()) createCsvFile();

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(directory));
            bufferedWriter.write(categoryExpense.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Fail", "writeOnCsvFile: "+ e.getMessage());
        }

    }

    @Override
    public List<CategoryExpense> readFromCsvFile() {
        return null;
    }
}
