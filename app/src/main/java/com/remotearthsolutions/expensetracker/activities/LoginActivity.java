package com.remotearthsolutions.expensetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.contracts.LoginContract;
import com.remotearthsolutions.expensetracker.presenters.LoginPresenter;
import com.remotearthsolutions.expensetracker.services.GoogleSigninServiceImpl;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginContract.View {


    private CallbackManager mCallbackManager;
    private static final String TAG = LoginActivity.class.getName();
    private FirebaseAuth mAuth;
    // Initialization for google sign in
    private GoogleSignInClient mGoogleSignInClient;
    private static final int code = 101;
    private Button googleSignInButton;
    private Button facebookSignInButton;

    private LoginPresenter presenter;

    private GoogleSigninServiceImpl googleSigninService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginPresenter(this,new GoogleSigninServiceImpl(this));
        presenter.init();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // add this code for google sign in
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == code) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }


    }


    // code for google sign in
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Login Successfully with google", Toast.LENGTH_LONG).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Login failed with google", Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.googlebutton:
                mGoogleSignInClient = presenter.getGoogleSignInClient();

                if (mGoogleSignInClient == null) {
                    Log.w(TAG, "mGoogleSignInClient must be initialized");
                    return;
                }

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, code);
                break;
            case R.id.facebook_login_button:

                break;
        }

    }


    @Override
    public void initializeView() {
        googleSignInButton = findViewById(R.id.googlebutton);
        facebookSignInButton = findViewById(R.id.facebook_login_button);

        googleSignInButton.setOnClickListener(this);
        facebookSignInButton.setOnClickListener(this);
    }

    @Override
    public void facebookInitialize() {
        mCallbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
    }
}
