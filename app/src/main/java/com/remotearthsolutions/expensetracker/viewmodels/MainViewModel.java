package com.remotearthsolutions.expensetracker.viewmodels;


import android.content.Intent;
import com.google.firebase.auth.FirebaseUser;
import com.remotearthsolutions.expensetracker.contracts.MainContract;
import com.remotearthsolutions.expensetracker.entities.User;
import com.remotearthsolutions.expensetracker.services.FirebaseService;


public class MainViewModel {

    private MainContract.View view;

    public MainViewModel(MainContract.View view) {
        this.view = view;
    }

    public void init() {
        view.initializeView();

    }

    public void checkAuthectication(FirebaseService firebaseService, User guestUser) {

        FirebaseUser user = firebaseService.getUser();

        if (user == null && guestUser == null)
        {
            view.openLoginScreen();
        }
    }
}
