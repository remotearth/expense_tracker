package com.remotearthsolutions.expensetracker.viewmodels;

import android.app.Activity;
import android.util.Base64;
import androidx.lifecycle.ViewModel;
import com.google.gson.Gson;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryExpenseDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import com.remotearthsolutions.expensetracker.services.FileProcessingService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import java.util.List;
import java.util.concurrent.Executors;

public class DashboardViewModel extends ViewModel {

    private CategoryExpenseDao categoryExpenseDao;
    private ExpenseDao expenseDao;
    private CategoryDao categoryDao;
    private AccountDao accountDao;
    private FileProcessingService fileProcessingService;
    private CompositeDisposable disposable = new CompositeDisposable();

    public DashboardViewModel(CategoryExpenseDao categoryExpenseDao, ExpenseDao expenseDao, CategoryDao categoryDao, AccountDao accountDao, FileProcessingService fileProcessingService) {
        this.categoryExpenseDao = categoryExpenseDao;
        this.expenseDao = expenseDao;
        this.categoryDao = categoryDao;
        this.accountDao = accountDao;
        this.fileProcessingService = fileProcessingService;
    }

    public void saveExpenseToCSV(Activity activity) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n\nDate,Category,Amount,From,Note\n");
        if (disposable.isDisposed()) {
            disposable = new CompositeDisposable();
        }
        disposable.add(categoryExpenseDao.getAllFilterExpense()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listOfFilterExpense -> {
                    if (listOfFilterExpense != null) {
                        for (int i = 0; i < listOfFilterExpense.size(); i++) {
                            if (listOfFilterExpense.get(i).getTotalAmount() > 0) {
                                stringBuilder.append(listOfFilterExpense.get(i));
                            }
                        }

                        stringBuilder.append("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                        stringBuilder.append("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                        stringBuilder.append("(Don't edit this meta data)\n");

                        disposable.add(expenseDao.getAllExpenseEntry().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(entries -> {
                            String json = new Gson().toJson(entries);
                            String encryptedStr = Base64.encodeToString(json.getBytes("UTF-8"), Base64.NO_WRAP);
                            stringBuilder.append("meta1:" + encryptedStr + "\n");

                            disposable.add(categoryDao.getAllCategories().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(
                                    entries1 -> {
                                        String json1 = new Gson().toJson(entries1);
                                        String encryptedStr1 = Base64.encodeToString(json1.getBytes("UTF-8"), Base64.NO_WRAP);
                                        stringBuilder.append("meta2:" + encryptedStr1 + "\n");

                                        disposable.add(accountDao.getAllAccounts().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(
                                                entries2 -> {
                                                    String json2 = new Gson().toJson(entries2);
                                                    String encryptedStr2 = Base64.encodeToString(json2.getBytes("UTF-8"), Base64.NO_WRAP);
                                                    stringBuilder.append("meta3:" + encryptedStr2 + "\n");
                                                    fileProcessingService.writeOnCsvFile(activity, stringBuilder.toString(), () -> {
                                                        shareCSV_FileToMail(activity);
                                                        disposable.dispose();
                                                    }, () -> {
                                                        disposable.dispose();
                                                        //show error - report was not generated. please try again
                                                    });
                                                }));
                                    }));
                        }));
                    }
                }));

    }

    public List<CategoryExpense> readExpenseFromCsv(Activity activity) {
        return fileProcessingService.readFromCsvFile(activity);
    }

    public List<String> getAllCsvFile() {
        return fileProcessingService.getNameOfAllCsvFile();
    }

    public void shareCSV_FileToMail(Activity activity) {
        fileProcessingService.shareFile(activity);
    }

    public void importDataFromFile(String filepath) {
        fileProcessingService.loadTableData(filepath, (categories, expenseModels, accountModels) -> {

            Executors.newSingleThreadExecutor().execute(() -> {

                categoryDao.deleteAll();
                for (CategoryModel categoryModel : categories) {
                    categoryDao.addCategory(categoryModel);
                }

                expenseDao.deleteAll();
                for (ExpenseModel expenseModel : expenseModels) {
                    expenseDao.add(expenseModel);
                }

                accountDao.deleteAll();
                for (AccountModel accountModel : accountModels) {
                    accountDao.addAccount(accountModel);
                }
            });

        });
    }
}
