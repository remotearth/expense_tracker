package com.remotearthsolutions.expensetracker.services;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils;
import com.remotearthsolutions.expensetracker.utils.PermissionUtils;

import java.io.*;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

public class FileProcessingServiceImp implements FileProcessingService {

    private PermissionUtils writePermission;

    public FileProcessingServiceImp() {
        writePermission = new PermissionUtils();
    }

    @Override
    public void writeOnCsvFile(Activity activity, String content) {
        writePermission.writeExternalStoragePermission(activity, new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                writeExternalFile(content);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                forceUserToGrantPermission(activity);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        });
    }

    @Override
    public List<CategoryExpense> readFromCsvFile(Activity activity) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), createFileNameAccordingToDate());
        List<CategoryExpense> categoryExpenseList = new ArrayList();

        try {
            String line = "";
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            fileReader.readLine();

            while ((line = fileReader.readLine()) != null) {
                String[] tokens = line.split(", ");

                if (tokens.length > 0) {
                    CategoryExpense categoryExpense = new CategoryExpense(Integer.parseInt(tokens[0]), tokens[1], tokens[2], Double.parseDouble(tokens[3]), Long.parseLong(tokens[4]));
                    categoryExpenseList.add(categoryExpense);
                }
            }

            fileReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return categoryExpenseList;
    }

    @Override
    public List<String> getNameOfAllCsvFile() {
        List<String> fileList = new ArrayList<>();

        File dataDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        String[] listOfAllItems = dataDirectory.list();

        for(String item : listOfAllItems) {
            if (item.contains("expense_tracker_")) {
                fileList.add(item);
            }
        }

        return fileList;
    }

    @Override
    public void shareFile(Activity activity) {
        String emailAddress = "abircoxsbazar@gmail.com";
        String emailSubject = "Reports From Expense Tracker";

        try {

            File fileLocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), createFileNameAccordingToDate());
            Uri uri = Uri.fromFile(fileLocation);

            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.setType("image/*");

            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailAddress});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, emailSubject);

            activity.startActivity(Intent.createChooser(emailIntent, "Choose Email Client To Send Report"));

        } catch (Throwable t) {
            Toast.makeText(activity, "Report Sending Failed Please Try Again Later " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void writeExternalFile(String content) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), createFileNameAccordingToDate());

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            Log.d("error", "File Not Found");
        } catch (IOException io) {
            Log.d("error", "Error File Creating");
        }

    }

    private void forceUserToGrantPermission(Activity activity) {
        writePermission.showSettingsDialog(activity);
    }

    private String createFileNameAccordingToDate() {
        return "expense_tracker_"+ DateTimeUtils.getCurrentDate(DateTimeUtils.dd_MM_yyyy) +".csv";
    }

}
