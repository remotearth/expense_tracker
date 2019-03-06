package com.remotearthsolutions.expensetracker.viewmodels;

import android.app.Activity;
import android.util.Base64;
import androidx.lifecycle.ViewModel;
import com.google.gson.Gson;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import com.remotearthsolutions.expensetracker.services.FileProcessingService;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DashboardViewModel extends ViewModel {
    private ExpenseDao expenseDao;
    private CategoryDao categoryDao;
    private AccountDao accountDao;
    private FileProcessingService fileProcessingService;
    private CompositeDisposable disposable = new CompositeDisposable();

    public DashboardViewModel(ExpenseDao expenseDao, CategoryDao categoryDao, AccountDao accountDao, FileProcessingService fileProcessingService) {
        this.expenseDao = expenseDao;
        this.categoryDao = categoryDao;
        this.accountDao = accountDao;
        this.fileProcessingService = fileProcessingService;
    }

    public void saveExpenseToCSV(Activity activity) {
        StringBuilder stringBuilder = new StringBuilder();

        disposable.add(expenseDao.getAllExpenseEntry().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(entries -> {
            String json = new Gson().toJson(entries);
            String encryptedStr = Base64.encodeToString(json.getBytes("UTF-8"), Base64.DEFAULT);
            stringBuilder.append("meta1:" + encryptedStr + "\n");

            categoryDao.getAllCategories().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(
                    entries1 -> {
                        String json1 = new Gson().toJson(entries1);
                        String encryptedStr1 = Base64.encodeToString(json1.getBytes("UTF-8"), Base64.DEFAULT);
                        stringBuilder.append("meta2:" + encryptedStr1 + "\n");

                        accountDao.getAllAccounts().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(
                                entries2 -> {
                                    String json2 = new Gson().toJson(entries2);
                                    String encryptedStr2 = Base64.encodeToString(json2.getBytes("UTF-8"), Base64.DEFAULT);
                                    stringBuilder.append("meta3:" + encryptedStr2 + "\n");
                                    stringBuilder.append("\n\n\n\n\n");
                                    expenseDao.getAllFilterExpense()
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(listOfFilterExpense -> {
                                                if (listOfFilterExpense != null) {
                                                    for (int i = 0; i < listOfFilterExpense.size(); i++) {
                                                        if (listOfFilterExpense.get(i).getTotal_amount() > 0) {
                                                            stringBuilder.append(listOfFilterExpense.get(i));
                                                        }
                                                    }

                                                    fileProcessingService.writeOnCsvFile(activity, stringBuilder.toString());
                                                }
                                            });
                                });
                    });
        }));

//        Observable.just(1, 2, 3, 4)
//                .doOnNext(integer -> {
//                    switch (integer) {
//                        case 1:
//                            expenseDao.getAllExpenseEntry().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(entries -> {
//                                String json = new Gson().toJson(entries);
//                                String encryptedStr = Base64.encodeToString(json.getBytes("UTF-8"), Base64.DEFAULT);
//                                stringBuilder.append("meta1:" + encryptedStr + "\n");
//                            });
//                            break;
//                        case 2:
//                            categoryDao.getAllCategories().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(
//                                    entries -> {
//                                        String json = new Gson().toJson(entries);
//                                        String encryptedStr = Base64.encodeToString(json.getBytes("UTF-8"), Base64.DEFAULT);
//                                        stringBuilder.append("meta2:" + encryptedStr + "\n");
//                                    });
//                            break;
//                        case 3:
//                            accountDao.getAllAccounts().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(
//                                    entries -> {
//                                        String json = new Gson().toJson(entries);
//                                        String encryptedStr = Base64.encodeToString(json.getBytes("UTF-8"), Base64.DEFAULT);
//                                        stringBuilder.append("meta3:" + encryptedStr + "\n");
//                                    });
//                            break;
//                        case 4:
//                            stringBuilder.append("\n\n\n");
//                            expenseDao.getAllFilterExpense()
//                                    .subscribeOn(Schedulers.io())
//                                    .observeOn(AndroidSchedulers.mainThread())
//                                    .subscribe(listOfFilterExpense -> {
//                                        if (listOfFilterExpense != null) {
//                                            for (int i = 0; i < listOfFilterExpense.size(); i++) {
//                                                if (listOfFilterExpense.get(i).getTotal_amount() > 0) {
//                                                    stringBuilder.append(listOfFilterExpense.get(i));
//                                                }
//                                            }
//
//                                            fileProcessingService.writeOnCsvFile(activity, stringBuilder.toString());
//                                        }
//                                    });
//                            break;
//
//                    }
//
//                }).subscribe();

//        disposable.add(expenseDao.getAllExpenseEntry().subscribeOn(Schedulers.io()).flatMap(entries -> {
//            String json = new Gson().toJson(entries);
//            String encryptedStr = Base64.encodeToString(json.getBytes("UTF-8"), Base64.DEFAULT);
//            stringBuilder.append("meta1:" + encryptedStr + "\n");
//
//        }));
//
//        disposable.add(categoryDao.getAllCategories().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(
//                entries -> {
//                    String json = new Gson().toJson(entries);
//                    String encryptedStr = Base64.encodeToString(json.getBytes("UTF-8"), Base64.DEFAULT);
//                    stringBuilder.append("meta2:" + encryptedStr + "\n");
//                }));
//
//        disposable.add(accountDao.getAllAccounts().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(
//                entries -> {
//                    String json = new Gson().toJson(entries);
//                    String encryptedStr = Base64.encodeToString(json.getBytes("UTF-8"), Base64.DEFAULT);
//                    stringBuilder.append("meta3:" + encryptedStr + "\n");
//                }));
//
//        stringBuilder.append("\n\n\n");
//
//        disposable.add(expenseDao.getAllFilterExpense()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(listOfFilterExpense -> {
//                    if (listOfFilterExpense != null) {
//                        for (int i = 0; i < listOfFilterExpense.size(); i++) {
//                            if (listOfFilterExpense.get(i).getTotal_amount() > 0) {
//                                stringBuilder.append(listOfFilterExpense.get(i));
//                            }
//                        }
//                    }
//                }));


    }

    public List<CategoryExpense> readExpenseFromCsv(Activity activity) {
        return fileProcessingService.readFromCsvFile(activity);
    }

    public void shareCSV_FileToMail(Activity activity) {
        fileProcessingService.shareFile(activity);
    }
}
