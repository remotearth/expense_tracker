package com.remotearthsolutions.expensetracker.di

import android.content.Context
import com.remotearthsolutions.expensetracker.contracts.MainContract
import com.remotearthsolutions.expensetracker.databaseutils.AppDatabase
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.services.FileProcessingServiceImp
import com.remotearthsolutions.expensetracker.services.FirebaseServiceImpl
import com.remotearthsolutions.expensetracker.viewmodels.mainview.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModels = module {
    viewModel { (view: MainContract.View) ->
        MainViewModel(
            view,
            FirebaseServiceImpl(get()),
            provideDb(get()).accountDao(),
            provideDb(get()).expenseDao(),
            provideDb(get()).categoryDao(),
            provideDb(get()).categoryExpenseDao(),
            FileProcessingServiceImp(get())
        )
    }
}

private fun provideDb(context: Context): AppDatabase {
    return DatabaseClient.getInstance(context).appDatabase
}
