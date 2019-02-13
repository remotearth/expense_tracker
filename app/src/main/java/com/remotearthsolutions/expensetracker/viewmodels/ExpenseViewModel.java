package com.remotearthsolutions.expensetracker.viewmodels;

import androidx.lifecycle.ViewModel;
import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import com.remotearthsolutions.expensetracker.entities.ExpeneChartData;
import com.remotearthsolutions.expensetracker.utils.Utils;
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

    public void loadFilterExpense(long startTime, long endTime)
    {
        disposable.add(expenseDao.getAllFilterExpense(startTime,endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((listOfFilterExpense) -> {

                    view.loadFilterExpense(listOfFilterExpense);

        }));
    }

}
