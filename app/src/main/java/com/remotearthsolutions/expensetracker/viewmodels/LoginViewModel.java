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
import com.remotearthsolutions.expensetracker.utils.Constants;

public class LoginViewModel extends ViewModel implements FacebookService.CallBack, FirebaseService.Callback, GoogleService.Callback {

    private LoginContract.View view;
    private GoogleService googleService;
    private FacebookService facebookService;
    private FirebaseService firebaseService;

    public LoginViewModel(LoginContract.View view, GoogleService googleService, FacebookService facebookService, FirebaseService firebaseService) {
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

        if (view.isDeviceOnline()) {
            facebookService.startFacebookLogin(this);
        } else {
            view.showAlert(Constants.KEY_WARNING, Constants.KEY_WARNING_MESSAGE, Constants.KEY_OK, null, null);
        }

    }

    public CallbackManager getFacebookCallbackManager() {
        return facebookService.getFacebookCallbackManager();
    }

    public void startGoogleLogin() {
        if (view.isDeviceOnline()) {
            view.loadUserEmails();
        } else {
            view.showAlert(Constants.KEY_WARNING, Constants.KEY_WARNING_MESSAGE, Constants.KEY_OK, null, null);
        }
    }

    public void googleLoginWithIntent(Intent data) {
        googleService.startGoogleLogin(data, this);
    }

    public GoogleSignInClient getGoogleSignInClient() {
        return googleService.getGoogleSignInClient();
    }

    @Override
    public void onFirebaseSigninSuccess(FirebaseUser user) {
        view.hideProgress();
        view.onLoginSuccess(user);
    }

    @Override
    public void onFirebaseSigninFailure(String message) {
        view.hideProgress();
        view.onLoginFailure();
        view.showAlert(null, message, Constants.KEY_OK, null, null);
    }

    @Override
    public void onSocialLoginSuccess(AuthCredential credential) {
        view.showProgress(Constants.KEY_PLEASE_WAIT);
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
