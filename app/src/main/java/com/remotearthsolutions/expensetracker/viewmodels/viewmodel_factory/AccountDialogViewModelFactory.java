package com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.remotearthsolutions.expensetracker.contracts.AccountDialogContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.viewmodels.AccountDialogViewModel;

public class AccountDialogViewModelFactory implements ViewModelProvider.Factory {

    private AccountDialogContract.View view;
    private AccountDao accountDao;

    public AccountDialogViewModelFactory(AccountDialogContract.View view, AccountDao accountDao) {
        this.view = view;
        this.accountDao = accountDao;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AccountDialogViewModel(view, accountDao);

    }
}
