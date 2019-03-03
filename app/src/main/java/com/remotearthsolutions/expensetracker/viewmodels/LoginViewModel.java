package com.remotearthsolutions.expensetracker.viewmodels;

import android.content.Intent;
import androidx.lifecycle.ViewModel;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.remotearthsolutions.expensetracker.contracts.LoginContract;
import com.remotearthsolutions.expensetracker.services.FacebookService;
import com.remotearthsolutions.expensetracker.services.FirebaseService;
import com.remotearthsolutions.expensetracker.services.GoogleService;
import com.remotearthsolutions.expensetracker.services.InternetCheckerService;

public class LoginViewModel extends ViewModel implements FacebookService.CallBack, FirebaseService.Callback, GoogleService.Callback {

    private LoginContract.View view;
    private GoogleService googleService;
    private FacebookService facebookService;
    private FirebaseService firebaseService;
    private InternetCheckerService internetCheckerService;

    public LoginViewModel(LoginContract.View view, GoogleService googleService, FacebookService facebookService, FirebaseService firebaseService, InternetCheckerService internetCheckerService) {
        this.view = view;
        this.googleService = googleService;
        this.facebookService = facebookService;
        this.firebaseService = firebaseService;
        this.internetCheckerService = internetCheckerService;
    }

    public void init() {

        view.initializeView();
        googleService.initializeGoogleSigninClient();
        facebookService.facebookCallbackInitialize();

    }

    public void startFacebookLogin() {

        if (internetCheckerService.isConnected())
        {
            facebookService.startFacebookLogin(this);
        }
        else
        {
            view.showAlert("Warning","No Internet Connection","OK",null,null);
        }

    }

    public CallbackManager getFacebookCallbackManager() {
        return facebookService.getFacebookCallbackManager();
    }

    public void startGoogleLogin(Intent data) {

        if (internetCheckerService.isConnected())
        {
            googleService.startGoogleLogin(data, this);
        }
        else
        {
            view.showAlert("Warning","No Internet Connection","OK",null,null);
        }

    }

    public void startGuestLogin() {

    }

    public GoogleSignInClient getGoogleSignInClient() {
        return googleService.getGoogleSignInClient();
    }

    @Override
    public void onFirebaseSigninSuccess(FirebaseUser user) {
        view.onLoginSuccess(user);
    }

    @Override
    public void onFirebaseSigninFailure(String message) {
        view.onLoginFailure();
        view.showAlert(null, message, "Ok", null, null);
    }

    @Override
    public void onSocialLoginSuccess(AuthCredential credential) {
        firebaseService.signinWithCredential(credential, this);
    }

    @Override
    public void onSocialLoginFailure(String message) {
        view.showAlert(null, message, "Ok", null, null);
    }

    @Override
    public void onFacebookLoginCancel() {
        view.onLoginFailure();
    }


}
