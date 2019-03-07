package com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.remotearthsolutions.expensetracker.contracts.HomeFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryExpenseDao;
import com.remotearthsolutions.expensetracker.viewmodels.HomeFragmentViewModel;

public class HomeFragmentViewModelFactory implements ViewModelProvider.Factory {

    private HomeFragmentContract.View view;
    private CategoryExpenseDao categoryExpenseDao;
    private CategoryDao categoryDao;
    private AccountDao accountDao;

    public HomeFragmentViewModelFactory(HomeFragmentContract.View view, CategoryExpenseDao categoryExpenseDao, CategoryDao categoryDao, AccountDao accountDao) {
        this.view = view;
        this.categoryExpenseDao = categoryExpenseDao;
        this.categoryDao = categoryDao;
        this.accountDao = accountDao;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new HomeFragmentViewModel(view,categoryExpenseDao,categoryDao,accountDao);
    }
}
