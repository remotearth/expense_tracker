package com.remotearthsolutions.expensetracker.services;

import android.content.Context;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public interface GoogleSigninService {
    void initializeGoogleSigninClient();
    GoogleSignInClient getGoogleSignInClient();
}
