package com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.remotearthsolutions.expensetracker.contracts.MainContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao;
import com.remotearthsolutions.expensetracker.services.FirebaseService;
import com.remotearthsolutions.expensetracker.viewmodels.MainViewModel;

public class MainViewModelFactory implements ViewModelProvider.Factory {

    private MainContract.View view;
    private FirebaseService firebaseService;
    private AccountDao accountDao;
    private ExpenseDao expenseDao;

    public MainViewModelFactory(MainContract.View view, FirebaseService firebaseService, AccountDao accountDao, ExpenseDao expenseDao) {
        this.view = view;
        this.firebaseService = firebaseService;
        this.accountDao = accountDao;
        this.expenseDao = expenseDao;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(view, firebaseService, accountDao, expenseDao);
    }
}
