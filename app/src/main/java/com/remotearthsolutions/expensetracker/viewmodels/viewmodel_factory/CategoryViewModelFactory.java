package com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.remotearthsolutions.expensetracker.contracts.CategoryFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.viewmodels.CategoryViewModel;

public class CategoryViewModelFactory implements ViewModelProvider.Factory {

    private CategoryFragmentContract.View view;
    private CategoryDao categoryDao;

    public CategoryViewModelFactory(CategoryFragmentContract.View view, CategoryDao categoryDao) {
        this.view = view;
        this.categoryDao = categoryDao;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CategoryViewModel(view, categoryDao);
    }
}
