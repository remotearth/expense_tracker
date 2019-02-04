package com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.remotearthsolutions.expensetracker.contracts.MainContract;
import com.remotearthsolutions.expensetracker.viewmodels.MainViewModel;

public class MainViewModelFactory implements ViewModelProvider.Factory {

    private MainContract.View view;

    public MainViewModelFactory(MainContract.View view) {
        this.view = view;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(view);
    }
}
