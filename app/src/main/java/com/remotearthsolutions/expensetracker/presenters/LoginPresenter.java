package com.remotearthsolutions.expensetracker.presenters;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.remotearthsolutions.expensetracker.contracts.LoginContract;
import com.remotearthsolutions.expensetracker.interactors.LoginInteractor;
import com.remotearthsolutions.expensetracker.services.FacebookLoginManagerImpl;
import com.remotearthsolutions.expensetracker.services.FacebookSignInService;
import com.remotearthsolutions.expensetracker.services.GoogleSigninService;

public class LoginPresenter implements FacebookLoginManagerImpl.CallBack {

    private  LoginContract.View view;
    private GoogleSigninService googleSigninService;
    private LoginContract.Interactor interactor;

    private FacebookSignInService facebookSignInService;

    public LoginPresenter(LoginContract.View view, GoogleSigninService googleSigninService, FacebookSignInService facebookSignInService){
        this.view = view;
        this.googleSigninService = googleSigninService;
        interactor = new LoginInteractor();
        this.facebookSignInService = facebookSignInService;


    }

    public void init() {

        googleSigninService.initializeGoogleSigninClient();
        view.initializeView();
        view.facebookInitialize();


    }

    public void startFacebookLogin()
    {
        facebookSignInService.initializeFacebookLoginManager(this);
    }

    public GoogleSignInClient getGoogleSignInClient() {
        return googleSigninService.getGoogleSignInClient();
    }

    @Override
    public void OnCallBackRegistrationSuccess(String token) {

        view.onTokenGenerated(token);

    }
}
