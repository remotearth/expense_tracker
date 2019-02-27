package com.remotearthsolutions.expensetracker.viewmodels;

import android.app.Activity;
import androidx.lifecycle.ViewModel;
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao;
import com.remotearthsolutions.expensetracker.services.FileProcessingService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class Tab2ViewModel extends ViewModel {
    private ExpenseDao expenseDao;
    private CompositeDisposable disposable;
    private FileProcessingService fileProcessingService;

    public Tab2ViewModel(ExpenseDao expenseDao, CompositeDisposable disposable, FileProcessingService fileProcessingService) {
        this.expenseDao = expenseDao;
        this.disposable = disposable;
        this.fileProcessingService = fileProcessingService;
    }

    public void saveExpenseToCSV(Activity activity) {
        final String[] content = {""};

        disposable.add(expenseDao.getAllFilterExpense()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listOfFilterExpense -> {
                    if (listOfFilterExpense != null) {
                        for (int i = 0; i < listOfFilterExpense.size(); i++) {
                            content[0] = content[0] + listOfFilterExpense.get(i);
                        }
                        fileProcessingService.writeOnCsvFile(activity, content[0]);
                    }
                }));
    }

    public void shareCSV_FileToMail(Activity activity) {
        fileProcessingService.shareFile(activity);
    }
}
