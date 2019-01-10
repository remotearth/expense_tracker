package com.remotearthsolutions.expensetracker.services;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.remotearthsolutions.expensetracker.activities.LoginActivity;

import java.util.Arrays;

import static com.facebook.login.widget.ProfilePictureView.TAG;

public class FacebookLoginManagerImpl implements FacebookSignInService {

    private CallbackManager mCallbackManager;
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public FacebookLoginManagerImpl(Context context) {
        this.context = context;

    }





    @Override
    public void initializeFacebookLoginManager(final CallBack callBack) {

        LoginManager.getInstance().logInWithReadPermissions((Activity) context, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("", "facebook:onSuccess:" + loginResult);
                callBack.OnCallBackRegistrationSuccess(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

    }

    public interface CallBack{

        void OnCallBackRegistrationSuccess(String token);

    }


}
