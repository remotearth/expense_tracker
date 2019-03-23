package com.remotearthsolutions.expensetracker.services;

import android.app.Activity;
import android.content.Context;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.remotearthsolutions.expensetracker.R;

public class FirebaseServiceImpl implements FirebaseService {

    private FirebaseAuth mAuth;
    private Context context;

    public FirebaseServiceImpl(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void signinWithCredential(AuthCredential credential, final Callback callback) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, task -> {
                    if (task.isSuccessful()) {
                        callback.onFirebaseSigninSuccess(task.getResult().getUser());

                    } else {
                        callback.onFirebaseSigninFailure(context.getString(R.string.Authentication_with_Firebase_is_failed));
                    }
                })
                .addOnFailureListener((Activity) context, e -> callback.onFirebaseSigninFailure(context.getString(R.string.Authentication_with_Firebase_is_failed)));
    }

    @Override
    public void signinAnonymously(Callback callback) {

        mAuth.signInAnonymously()
                .addOnCompleteListener((Activity) context, task -> {
                    if (task.isSuccessful()) {
                        callback.onFirebaseSigninSuccess(task.getResult().getUser());
                    } else {
                        callback.onFirebaseSigninFailure(context.getString(R.string.Authentication_with_Firebase_is_failed));
                    }
                }).addOnFailureListener((Activity) context, e -> callback.onFirebaseSigninFailure(context.getString(R.string.Authentication_with_Firebase_is_failed)));

    }

    @Override
    public FirebaseUser getUser() {
        if (mAuth == null) {
            return null;
        }
        return mAuth.getCurrentUser();
    }

    @Override
    public void logout() {
        if (mAuth != null) {
            mAuth.signOut();
        }
    }
}
