package com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.remotearthsolutions.expensetracker.contracts.AccountContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.viewmodels.AccountViewModel;

public class AccountViewModelFactory implements ViewModelProvider.Factory {

    private Context context;
    private AccountContract.View view;
    private AccountDao accountDao;

    public AccountViewModelFactory(Context context, AccountContract.View view,AccountDao accountDao) {
        this.context =context;
        this.view = view;
        this.accountDao = accountDao;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AccountViewModel(context, view,accountDao);

    }
}
