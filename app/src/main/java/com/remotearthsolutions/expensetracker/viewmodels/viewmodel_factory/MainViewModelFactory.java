package com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.remotearthsolutions.expensetracker.contracts.MainContract;
import com.remotearthsolutions.expensetracker.services.FirebaseService;
import com.remotearthsolutions.expensetracker.viewmodels.MainViewModel;

public class MainViewModelFactory implements ViewModelProvider.Factory {

    private MainContract.View view;
    private FirebaseService firebaseService;

    public MainViewModelFactory(MainContract.View view, FirebaseService firebaseService) {
        this.view = view;
        this.firebaseService = firebaseService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(view, firebaseService);
    }
}
