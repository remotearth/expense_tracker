package com.remotearthsolutions.expensetracker.services;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import com.remotearthsolutions.expensetracker.utils.PermissionUtils;

import java.io.*;
import java.util.List;

public class FileProcessingServiceImp implements FileProcessingService {

    private static final String FILE_DIRECTORY = "My_Expense";
    private static final String FILE_NAME = "expenses.csv";
    private File directory;
    private PermissionUtils writePermission;

    public FileProcessingServiceImp() {
        directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), FILE_DIRECTORY);
        writePermission = new PermissionUtils();
    }

    @Override
    public void createCsvFile() {
        if (!directory.exists()) {
            try {
                directory.mkdirs();
            } catch (Exception e) {
                Log.d("FileOpeningFailed", "" + e.getMessage());
            }
        }
    }

    @Override
    public void writeOnCsvFile(Activity activity, CategoryExpense categoryExpense) {
        writePermission.writeExternalStoragePermission(activity, new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                writeExternalFile(categoryExpense);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        });
    }

    private void writeExternalFile(CategoryExpense categoryExpense) {
        if (!directory.exists()) createCsvFile();

        try {

            FileWriter writer = new FileWriter(new File(directory +"/"+ FILE_NAME));
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(categoryExpense.toString());
            Log.d("Category", categoryExpense.toString());
            bufferedWriter.close();

        } catch (IOException e) {
            Log.d("WriteFail", "" + e.getMessage());
        }
    }

    @Override
    public List<CategoryExpense> readFromCsvFile() {
        return null;
    }

}
