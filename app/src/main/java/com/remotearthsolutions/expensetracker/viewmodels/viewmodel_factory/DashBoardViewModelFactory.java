package com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryExpenseDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao;
import com.remotearthsolutions.expensetracker.services.FileProcessingService;
import com.remotearthsolutions.expensetracker.viewmodels.DashboardViewModel;

public class DashBoardViewModelFactory implements ViewModelProvider.Factory {

    private CategoryExpenseDao categoryExpenseDao;
    private ExpenseDao expenseDao;
    private CategoryDao categoryDao;
    private AccountDao accountDao;
    private FileProcessingService fileProcessingService;

    public DashBoardViewModelFactory(CategoryExpenseDao categoryExpenseDao, ExpenseDao expenseDao, CategoryDao categoryDao, AccountDao accountDao, FileProcessingService fileProcessingService) {
        this.categoryExpenseDao = categoryExpenseDao;
        this.expenseDao = expenseDao;
        this.categoryDao = categoryDao;
        this.accountDao = accountDao;
        this.fileProcessingService = fileProcessingService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DashboardViewModel(categoryExpenseDao, expenseDao, categoryDao, accountDao, fileProcessingService);
    }
}
