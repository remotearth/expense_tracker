package com.remotearthsolutions.expensetracker.viewmodels;


import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.remotearthsolutions.expensetracker.contracts.MainContract;
import com.remotearthsolutions.expensetracker.entities.User;
import com.remotearthsolutions.expensetracker.services.FirebaseService;


public class MainViewModel extends ViewModel {

    private MainContract.View view;
    private FirebaseService firebaseService;

    public MainViewModel(MainContract.View view,FirebaseService firebaseService) {
        this.view = view;
        this.firebaseService = firebaseService;
    }

    public void init() {
        view.initializeView();
    }

    public void checkAuthectication(User guestUser) {

        FirebaseUser user = firebaseService.getUser();

        if (user == null && guestUser == null) {
            view.goBackToLoginScreen();
        }
        else{
            view.startLoadingApp();
        }
    }

    public void performLogout() {
        firebaseService.logout();
        view.onLogoutSuccess();
    }
}
