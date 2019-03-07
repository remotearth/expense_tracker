package com.remotearthsolutions.expensetracker.viewmodels;

import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ExpenseFragmentViewModel {

    private ExpenseFragmentContract.View view;
    private ExpenseDao expenseDao;
    private AccountDao accountDao;
    private CategoryDao categoryDao;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ExpenseFragmentViewModel(ExpenseFragmentContract.View view, ExpenseDao expenseDao, AccountDao accountDao, CategoryDao categoryDao) {
        this.view = view;
        this.expenseDao = expenseDao;
        this.accountDao = accountDao;
        this.categoryDao = categoryDao;
    }

    public void init(int accountId) {
        view.defineClickListener();

        compositeDisposable.add(accountDao.getAccountById(accountId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(accountIncome -> {
                    if (accountIncome != null) {
                        view.setSourceAccount(accountIncome);
                    }
                }));
    }

    public void addExpense(ExpenseModel expense) {

        if (Math.abs(expense.getAmount()) != 0) {

            compositeDisposable.add(Completable.fromAction(() -> {
                if (expense.getId() > 0) {
                    expenseDao.updateExpenseAmount(expense);
                } else {
                    expenseDao.add(expense);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> view.onExpenseAdded()));
        } else {
            view.showToast("Please enter an amount");
        }
    }

    public void setDefaultCategory() {
        compositeDisposable.add(categoryDao.getAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).
                        subscribe(categoryModels -> {
                            if (categoryModels != null && categoryModels.size() > 0) {
                                CategoryModel categoryModel = categoryModels.get(0);
                                view.showDefaultCategory(categoryModel);
                            }

                        }));

    }
}
