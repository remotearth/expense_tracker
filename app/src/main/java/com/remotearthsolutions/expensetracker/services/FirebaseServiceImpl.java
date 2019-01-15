package com.remotearthsolutions.expensetracker.services;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseServiceImpl implements FirebaseService {

    private FirebaseAuth mAuth;
    private Context context;

    public FirebaseServiceImpl(Context context) {
        this.context = context;

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void signinWithCredential(AuthCredential credential,final Callback callback) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            callback.onFirebaseSigninSuccess(task.getResult().getUser());

                        } else {
                            callback.onFirebaseSigninFailure("Authentication with Firebase is failed ");
                        }
                    }
                })
                .addOnFailureListener((Activity)context, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFirebaseSigninFailure("Authentication with Firebase is failed ");
                    }
                });
    }
}
