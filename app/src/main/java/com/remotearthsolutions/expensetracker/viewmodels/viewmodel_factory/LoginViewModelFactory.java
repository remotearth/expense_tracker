package com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.remotearthsolutions.expensetracker.contracts.LoginContract;
import com.remotearthsolutions.expensetracker.viewmodels.LoginViewModel;
import com.remotearthsolutions.expensetracker.services.FacebookService;
import com.remotearthsolutions.expensetracker.services.FirebaseService;
import com.remotearthsolutions.expensetracker.services.GoogleService;

public class LoginViewModelFactory implements ViewModelProvider.Factory {

    private Context context;
    private LoginContract.View view;
    private GoogleService googleService;
    private FacebookService facebookService;
    private FirebaseService firebaseService;

    public LoginViewModelFactory(Context context,LoginContract.View view, GoogleService googleService, FacebookService facebookService, FirebaseService firebaseService) {
        this.context = context;
        this.view = view;
        this.googleService = googleService;
        this.facebookService = facebookService;
        this.firebaseService = firebaseService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LoginViewModel(context,view, googleService, facebookService, firebaseService);
    }
}
