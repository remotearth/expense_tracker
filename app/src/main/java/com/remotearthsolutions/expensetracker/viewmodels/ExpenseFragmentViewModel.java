package com.remotearthsolutions.expensetracker.viewmodels;

import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ExpenseFragmentViewModel {

    private ExpenseFragmentContract.View view;
    private ExpenseDao expenseDao;

    public ExpenseFragmentViewModel(ExpenseFragmentContract.View view, ExpenseDao expenseDao) {
        this.view = view;
        this.expenseDao = expenseDao;
    }

    public void init() {
        view.defineClickListener();
    }

    public void addExpense(ExpenseModel expense) {

        if (Math.abs(expense.getAmount()) != 0) {

            Completable.fromAction(() -> {
                if (expense.getId()>0) {
                    expenseDao.updateExpenseAmount(expense);
                } else {
                    expenseDao.add(expense);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> view.onExpenseAdded());
        }
    }
}
