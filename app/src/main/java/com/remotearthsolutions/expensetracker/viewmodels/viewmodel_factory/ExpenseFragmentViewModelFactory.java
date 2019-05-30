package com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao;
import com.remotearthsolutions.expensetracker.viewmodels.ExpenseFragmentViewModel;

public class ExpenseFragmentViewModelFactory implements ViewModelProvider.Factory {

    private Context context;
    private ExpenseFragmentContract.View view;
    private ExpenseDao expenseDao;
    private AccountDao accountDao;
    private CategoryDao categoryDao;

    public ExpenseFragmentViewModelFactory(Context context, ExpenseFragmentContract.View view, ExpenseDao expenseDao, AccountDao accountDao, CategoryDao categoryDao) {
        this.context = context;
        this.view = view;
        this.expenseDao = expenseDao;
        this.accountDao = accountDao;
        this.categoryDao = categoryDao;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ExpenseFragmentViewModel(context, view, expenseDao, accountDao, categoryDao);
    }
}

