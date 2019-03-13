package com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.remotearthsolutions.expensetracker.contracts.AccountContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.viewmodels.AccountViewModel;

public class AccountViewModelFactory implements ViewModelProvider.Factory {

    private AccountContract.View view;
    private AccountDao accountDao;

    public AccountViewModelFactory(AccountContract.View view, AccountDao accountDao) {
        this.view = view;
        this.accountDao = accountDao;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AccountViewModel(view, accountDao);

    }
}
