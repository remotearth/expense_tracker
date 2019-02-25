package com.remotearthsolutions.expensetracker.viewmodels;

import android.app.Activity;
import androidx.lifecycle.ViewModel;
import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import com.remotearthsolutions.expensetracker.services.FileProcessingService;
import com.remotearthsolutions.expensetracker.services.FileProcessingServiceImp;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

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


    public void saveExpenseToCSV(Activity activity, long startTime, long endTime) {

        disposable.add(expenseDao.getAllFilterExpense(startTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listOfFilterExpense -> {

                    if (listOfFilterExpense != null) {
                        for (int i = 0; i < listOfFilterExpense.size(); i++) {
                            fileProcessingService.writeOnCsvFile(activity, listOfFilterExpense.get(i));
                        }
                    }

                }));
    }

}




