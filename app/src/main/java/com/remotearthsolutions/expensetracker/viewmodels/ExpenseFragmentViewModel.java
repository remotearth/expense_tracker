package com.remotearthsolutions.expensetracker.viewmodels;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ExpenseFragmentViewModel extends ViewModel {

    private Context context;
    private ExpenseFragmentContract.View view;
    private ExpenseDao expenseDao;
    private AccountDao accountDao;
    private CategoryDao categoryDao;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ExpenseFragmentViewModel(Context context, ExpenseFragmentContract.View view,ExpenseDao expenseDao, AccountDao accountDao, CategoryDao categoryDao) {
        this.context = context;
        this.view = view;
        this.expenseDao = expenseDao;
        this.accountDao = accountDao;
        this.categoryDao = categoryDao;
    }

    public void init() {
        view.defineClickListener();
    }

    public void setDefaultSourceAccount(int accountId) {
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
                    .subscribe(() -> view.onExpenseAdded(expense.getAmount())));
        } else {
            view.showToast(context.getString(R.string.please_enter_an_amount));
        }
    }

    public void updateAccountAmount(int accountId, double amount) {
        compositeDisposable.add(accountDao.getAccountById(accountId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(accountModel -> {
                    double previousAmount = accountModel.getAmount();
                    previousAmount -= amount;
                    accountModel.setAmount(previousAmount);

                    Completable.fromAction(() -> accountDao.updateAccount(accountModel)).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                            });

                })
        );
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

    public void deleteExpense(CategoryExpense categoryExpense) {

        if (categoryExpense == null) {
            return;
        }

        compositeDisposable.add(Completable.fromAction(() -> {
            expenseDao.delete(categoryExpense.getExpenseId());
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.onExpenseDeleted(categoryExpense)));


    }
}
