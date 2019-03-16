package com.remotearthsolutions.expensetracker.services;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import com.google.gson.Gson;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.remotearthsolutions.expensetracker.contracts.BaseView;
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils;
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils;
import com.remotearthsolutions.expensetracker.utils.PermissionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileProcessingServiceImp implements FileProcessingService {

    private PermissionUtils writePermission;

    public FileProcessingServiceImp() {
        writePermission = new PermissionUtils();
    }

    @Override
    public void writeOnCsvFile(Activity activity, String content, Runnable onSuccessRunnable, Runnable onFailureRunnable) {
        writePermission.writeExternalStoragePermission(activity, new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                if (writeExternalFile(content)) {
                    if (onSuccessRunnable != null) {
                        onSuccessRunnable.run();
                    }
                } else {
                    if (onFailureRunnable != null) {
                        onFailureRunnable.run();
                    }
                }
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                if (response.isPermanentlyDenied()) {
                    forceUserToGrantPermission(activity);
                } else {
                    AlertDialogUtils.show(activity, "",
                            "Without this permission, the app cannot export data.",
                            "Ok", null, null);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                AlertDialogUtils.show(activity, "Attention",
                        "Writing to external storage permission is needed to export data. Do you want to continue by giving the permission?",
                        "Yes",
                        "No",
                        new BaseView.Callback() {
                            @Override
                            public void onOkBtnPressed() {
                                token.continuePermissionRequest();
                            }

                            @Override
                            public void onCancelBtnPressed() {
                                token.cancelPermissionRequest();
                            }
                        });
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
            Log.d("Exception", "" + e.getMessage());
        }

        return categoryExpenseList;
    }

    @Override
    public void loadTableData(String filepath, Callback callback) {

        List<ExpenseModel> expenseModels = null;
        List<CategoryModel> categoryModels = null;
        List<AccountModel> accountModels = null;

        File file = new File(filepath);
        BufferedReader fileReader = null;

        try {
            String line;
            fileReader = new BufferedReader(new FileReader(file));
            fileReader.readLine();

            while ((line = fileReader.readLine()) != null) {
                if (line.contains("meta1")) {
                    line = line.replace("meta1:", "");
                    String jsonContent = new String(Base64.decode(line, Base64.NO_WRAP), "UTF-8");
                    expenseModels = Arrays.asList(new Gson().fromJson(jsonContent, ExpenseModel[].class));
                } else if (line.contains("meta2")) {
                    line = line.replace("meta2:", "");
                    String jsonContent = new String(Base64.decode(line, Base64.NO_WRAP), "UTF-8");
                    categoryModels = Arrays.asList(new Gson().fromJson(jsonContent, CategoryModel[].class));
                } else if (line.contains("meta3")) {
                    line = line.replace("meta3:", "");
                    String jsonContent = new String(Base64.decode(line, Base64.NO_WRAP), "UTF-8");
                    accountModels = Arrays.asList(new Gson().fromJson(jsonContent, AccountModel[].class));
                }
            }

            callback.onComplete(categoryModels, expenseModels, accountModels);
        } catch (Exception e) {
            Log.d("Exception", "" + e.getMessage());
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                Log.d("Exception", "" + e.getMessage());
            }
        }
    }

    public List<String> getNameOfAllCsvFile() {
        List<String> fileList = new ArrayList<>();

        File dataDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        String[] listOfAllItems = dataDirectory.list();
        if (listOfAllItems != null && listOfAllItems.length > 0) {
            for (String item : listOfAllItems) {
                if (item.contains("expense_tracker_")) {
                    fileList.add(item);
                }
            }
        }

        return fileList;
    }

    @Override
    public void shareFile(Activity activity) {
        String emailAddress = "";
        String emailSubject = "Reports From Expense Tracker";

        try {
            File fileLocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), createFileNameAccordingToDate());
            Uri uri = FileProvider.getUriForFile(activity, "com.remotearthsolutions.expensetracker.provider", fileLocation);

            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");

            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailAddress});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, emailSubject);

            activity.startActivity(Intent.createChooser(emailIntent, "Choose Email Client To Send Report"));

        } catch (Exception t) {
            Toast.makeText(activity, "Report Sending Failed Please Try Again Later ", Toast.LENGTH_LONG).show();
        }
    }

    private boolean writeExternalFile(String content) {

        boolean isSuccess = true;

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), createFileNameAccordingToDate());

        FileWriter fw = null;
        PrintWriter printWriter = null;
        try {
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }

            fw = new FileWriter(file, true);
            printWriter = new PrintWriter(fw);
            printWriter.print(content);

        } catch (FileNotFoundException e) {
            Log.d("error", "File Not Found");
            isSuccess = false;
        } catch (IOException io) {
            Log.d("error", "Error File Creating");
            isSuccess = false;
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                    printWriter.close();
                } catch (IOException e) {
                    Log.d("error", "Error File Creating" + e.getMessage());
                }
            }
        }

        return isSuccess;
    }

    private void forceUserToGrantPermission(Activity activity) {
        writePermission.showSettingsDialog(activity);
    }

    private String createFileNameAccordingToDate() {
        return "expense_tracker_" + DateTimeUtils.getCurrentDate(DateTimeUtils.dd_MM_yyyy) + ".csv";
    }

}
