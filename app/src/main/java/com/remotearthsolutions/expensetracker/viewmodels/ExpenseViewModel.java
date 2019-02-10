package com.remotearthsolutions.expensetracker.viewmodels;

import androidx.lifecycle.ViewModel;
import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ExpenseViewModel extends ViewModel {

    private ExpenseFragmentContract.ExpenseView view;
    private ExpenseDao expenseDao;
    private CompositeDisposable disposable = new CompositeDisposable();

    public ExpenseViewModel(ExpenseFragmentContract.ExpenseView view, ExpenseDao expenseDao) {
        this.view = view;
        this.expenseDao = expenseDao;
    }

    public void showExpense() {
                disposable.add(expenseDao.getAllExpense()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        expense -> view.showExpense(expense)
                ));
    }

}
