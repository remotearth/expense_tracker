package com.remotearthsolutions.expensetracker.di

import android.content.Context
import com.remotearthsolutions.expensetracker.contracts.LoginContract
import com.remotearthsolutions.expensetracker.contracts.MainContract
import com.remotearthsolutions.expensetracker.databaseutils.AppDatabase
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.services.FacebookServiceImpl
import com.remotearthsolutions.expensetracker.services.FileProcessingServiceImp
import com.remotearthsolutions.expensetracker.services.FirebaseServiceImpl
import com.remotearthsolutions.expensetracker.services.GoogleServiceImpl
import com.remotearthsolutions.expensetracker.viewmodels.LoginViewModel
import com.remotearthsolutions.expensetracker.viewmodels.mainview.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModels = module {
    viewModel { (context:Context, view: MainContract.View) ->
        MainViewModel(
            view,
            FirebaseServiceImpl(context),
            provideDb(context).accountDao(),
            provideDb(context).expenseDao(),
            provideDb(context).categoryDao(),
            provideDb(context).categoryExpenseDao(),
            FileProcessingServiceImp(context)
        )
    }

    viewModel { (context: Context, view: LoginContract.View) ->
        LoginViewModel(
            context,
            view,
            GoogleServiceImpl(context),
            FacebookServiceImpl(context),
            FirebaseServiceImpl(context)
        )
    }
}

private fun provideDb(context: Context): AppDatabase {
    return DatabaseClient.getInstance(context).appDatabase
}
