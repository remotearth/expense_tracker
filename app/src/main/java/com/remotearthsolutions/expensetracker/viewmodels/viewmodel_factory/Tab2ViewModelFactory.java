package com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao;
import com.remotearthsolutions.expensetracker.services.FileProcessingService;
import com.remotearthsolutions.expensetracker.viewmodels.DashboardViewModel;
import io.reactivex.disposables.CompositeDisposable;

public class Tab2ViewModelFactory implements ViewModelProvider.Factory {

    private ExpenseDao expenseDao;
    private CompositeDisposable disposable;
    private FileProcessingService fileProcessingService;

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DashboardViewModel(expenseDao, disposable, fileProcessingService);
    }
}
