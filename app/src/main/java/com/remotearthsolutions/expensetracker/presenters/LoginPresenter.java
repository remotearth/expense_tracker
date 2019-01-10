package com.remotearthsolutions.expensetracker.presenters;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.remotearthsolutions.expensetracker.contracts.LoginContract;
import com.remotearthsolutions.expensetracker.interactors.LoginInteractor;
import com.remotearthsolutions.expensetracker.services.GoogleSigninService;

public class LoginPresenter {

    private  LoginContract.View view;
    private GoogleSigninService googleSigninService;
    private LoginContract.Interactor interactor;
    public LoginPresenter(LoginContract.View view, GoogleSigninService googleSigninService){
        this.view = view;
        this.googleSigninService = googleSigninService;
        interactor = new LoginInteractor();
    }

    public void init() {

        googleSigninService.initializeGoogleSigninClient();
        view.initializeView();
        view.facebookInitialize();
    }

    public GoogleSignInClient getGoogleSignInClient() {
        return googleSigninService.getGoogleSignInClient();
    }
}
