package com.remotearthsolutions.expensetracker.services;

import android.content.Intent;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public interface GoogleService {
    void initializeGoogleSigninClient();
    void startGoogleLogin(Intent data,Callback callback);
    GoogleSignInClient getGoogleSignInClient();

    interface Callback extends SocialLoginCallback{
    }
}
