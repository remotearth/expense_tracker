package com.remotearthsolutions.expensetracker.services;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;

public interface FirebaseService {
    void signinWithCredential(AuthCredential token, Callback callback);

    interface Callback{
        void onFirebaseSigninSuccess(FirebaseUser user);
        void onFirebaseSigninFailure(String message);
    }
}
