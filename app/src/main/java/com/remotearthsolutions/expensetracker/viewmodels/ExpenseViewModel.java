package com.remotearthsolutions.expensetracker.viewmodels;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.DateModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

public class ExpenseViewModel extends ViewModel {

    private ExpenseFragmentContract.ExpenseView view;
    private ExpenseDao expenseDao;
    private CompositeDisposable disposable = new CompositeDisposable();


    public ExpenseViewModel(ExpenseFragmentContract.ExpenseView view, ExpenseDao expenseDao) {
        this.view = view;
        this.expenseDao = expenseDao;
    }

    public void loadFilterExpense(long startTime, long endTime) {
        disposable.add(expenseDao.getExpenseWithinRange(startTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((listOfFilterExpense) -> {

                    List<CategoryExpense> expenseList = new ArrayList<>();

                    if (listOfFilterExpense.size() > 0) {
                        long previousDate = listOfFilterExpense.get(0).getDatetime();
                        CategoryExpense header = new CategoryExpense();
                        header.isHeader = true;
                        header.setDatetime(previousDate);
                        expenseList.add(header);

                        for (int i = 0; i < listOfFilterExpense.size(); i++) {
                            CategoryExpense expense = listOfFilterExpense.get(i);

                            if (expense.getDatetime() != previousDate) {
                                CategoryExpense dummy = new CategoryExpense();
                                dummy.isHeader = true;
                                dummy.setDatetime(expense.getDatetime());
                                previousDate = expense.getDatetime();
                                expenseList.add(dummy);
                            }

                            expenseList.add(expense);
                        }
                    }

                    view.loadFilterExpense(expenseList);

                }));
    }

    public void loadFilterDate(long startTime, long endTime) {
        disposable.add(expenseDao.getDateWithinRange(startTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listOfDate -> {

                    List<DateModel> dateModelList = new ArrayList<>();
                    if (listOfDate != null) {
                        for (int i = 0; i < listOfDate.size(); i++) {
                            dateModelList.add(listOfDate.get(i));
                            Log.d("Date", " "+ listOfDate.get(i).getDate());
                        }
                    }

                    view.loadDate(dateModelList);

                }));
    }

}




