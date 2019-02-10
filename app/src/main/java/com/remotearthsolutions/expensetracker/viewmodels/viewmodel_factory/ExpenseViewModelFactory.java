package com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao;
import com.remotearthsolutions.expensetracker.viewmodels.ExpenseViewModel;

public class ExpenseViewModelFactory implements ViewModelProvider.Factory  {


    private ExpenseFragmentContract.ExpenseView view;
    private ExpenseDao expenseDao;

    public ExpenseViewModelFactory(ExpenseFragmentContract.ExpenseView view, ExpenseDao expenseDao) {
        this.view = view;
        this.expenseDao = expenseDao;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ExpenseViewModel(view, expenseDao);
    }


}
