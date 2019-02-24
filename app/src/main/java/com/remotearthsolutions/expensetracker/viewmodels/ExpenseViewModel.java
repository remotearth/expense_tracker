package com.remotearthsolutions.expensetracker.viewmodels;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import com.remotearthsolutions.expensetracker.services.FileProcessingService;
import com.remotearthsolutions.expensetracker.services.FileProcessingServiceImp;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ExpenseViewModel extends ViewModel {

    private ExpenseFragmentContract.ExpenseView view;
    private ExpenseDao expenseDao;
    private CompositeDisposable disposable = new CompositeDisposable();
    private FileProcessingService fileProcessingService;

    public ExpenseViewModel(ExpenseFragmentContract.ExpenseView view, ExpenseDao expenseDao) {
        this.view = view;
        this.expenseDao = expenseDao;

        fileProcessingService = new FileProcessingServiceImp();
    }

    public void loadFilterExpense(long startTime, long endTime) {
        disposable.add(expenseDao.getAllFilterExpense(startTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((listOfFilterExpense) -> {

                    List<CategoryExpense> expenseList = new ArrayList<>();
                    if (listOfFilterExpense != null) {

                        for (int i = 0; i < listOfFilterExpense.size(); i++) {
                            CategoryExpense expense = listOfFilterExpense.get(i);
                            if (expense.getTotal_amount() > 0) {
                                expenseList.add(expense);
                            }
                        }
                    }

                    view.loadFilterExpense(expenseList);

                }));
    }


    public void saveExpenseToCSV(long startTime, long endTime) {

        try {
            FileOutputStream outputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/raw/expense.csv"), true);
            disposable.add(expenseDao.getAllFilterExpense(startTime, endTime)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listOfFilterExpense -> {

                        if (listOfFilterExpense != null) {
                            for (int i = 0; i < listOfFilterExpense.size(); i++) {
                                Log.d("Info", ""+ listOfFilterExpense.get(i));
                                fileProcessingService.writeOnCsvFile(listOfFilterExpense.get(i));
                            }
                        }

                    }));

            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Failed", "saveExpenseToCSV: "+ e.getMessage());
        }
    }

}




