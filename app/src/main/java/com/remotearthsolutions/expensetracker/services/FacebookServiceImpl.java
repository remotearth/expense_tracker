package com.remotearthsolutions.expensetracker.services;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.remotearthsolutions.expensetracker.R;

import java.util.Arrays;

import static com.facebook.login.widget.ProfilePictureView.TAG;

public class FacebookServiceImpl implements FacebookService {

    private CallbackManager mCallbackManager;
    private Context context;

    public FacebookServiceImpl(Context context) {
        this.context = context;
    }


    @Override
    public void facebookCallbackInitialize() {
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void startFacebookLogin(final CallBack callBack) {

        LoginManager.getInstance().logInWithReadPermissions((Activity) context, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("", "facebook:onSuccess:" + loginResult);
                String token = loginResult.getAccessToken().getToken();
                AuthCredential credential = FacebookAuthProvider.getCredential(token);
                callBack.onSocialLoginSuccess(credential);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                callBack.onFacebookLoginCancel();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                callBack.onSocialLoginFailure(error.getMessage());
            }
        });

    }

    @Override
    public CallbackManager getFacebookCallbackManager() {
        return mCallbackManager;
    }


}
