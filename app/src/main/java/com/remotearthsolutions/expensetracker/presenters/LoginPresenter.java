package com.remotearthsolutions.expensetracker.presenters;

import android.content.Intent;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.remotearthsolutions.expensetracker.contracts.LoginContract;
import com.remotearthsolutions.expensetracker.services.FacebookService;
import com.remotearthsolutions.expensetracker.services.FirebaseService;
import com.remotearthsolutions.expensetracker.services.GoogleService;

public class LoginPresenter extends ViewModel implements FacebookService.CallBack, FirebaseService.Callback, GoogleService.Callback {

    private LoginContract.View view;
    private GoogleService googleService;
    private FacebookService facebookService;
    private FirebaseService firebaseService;

    public LoginPresenter(LoginContract.View view, GoogleService googleService, FacebookService facebookService, FirebaseService firebaseService) {
        this.view = view;
        this.googleService = googleService;
        this.facebookService = facebookService;
        this.firebaseService = firebaseService;
    }

    public void init() {

        view.initializeView();
        googleService.initializeGoogleSigninClient();
        facebookService.facebookCallbackInitialize();

    }

    public void startFacebookLogin() {
        facebookService.startFacebookLogin(this);
    }

    public CallbackManager getFacebookCallbackManager() {
        return facebookService.getFacebookCallbackManager();
    }

    public void startGoogleLogin(Intent data) {
        googleService.startGoogleLogin(data, this);
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
