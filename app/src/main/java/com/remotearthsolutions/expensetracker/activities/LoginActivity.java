package com.remotearthsolutions.expensetracker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.contracts.LoginContract;
import com.remotearthsolutions.expensetracker.entities.User;
import com.remotearthsolutions.expensetracker.services.FacebookServiceImpl;
import com.remotearthsolutions.expensetracker.services.FirebaseServiceImpl;
import com.remotearthsolutions.expensetracker.services.GoogleServiceImpl;
import com.remotearthsolutions.expensetracker.utils.Constants;
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils;
import com.remotearthsolutions.expensetracker.viewmodels.LoginViewModel;
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.LoginViewModelFactory;

public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginContract.View {

    private static final String TAG = LoginActivity.class.getName();
    private static final int REQUEST_CODE_GOOGLE_SIGNIN_INTENT = 101;

    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewModel = ViewModelProviders.of(this,
                new LoginViewModelFactory(this, new GoogleServiceImpl(this), new FacebookServiceImpl(this), new FirebaseServiceImpl(this))).
                get(LoginViewModel.class);
        viewModel.init();

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        viewModel.getFacebookCallbackManager().onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GOOGLE_SIGNIN_INTENT) {
            viewModel.startGoogleLogin(data);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.googlebutton:
                GoogleSignInClient mGoogleSignInClient = viewModel.getGoogleSignInClient();

                if (mGoogleSignInClient == null) {
                    Log.w(TAG, "mGoogleSignInClient must be initialized");
                    return;
                }

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE_SIGNIN_INTENT);
                break;

            case R.id.facebook_login_button:
                viewModel.startFacebookLogin();
                break;

            case R.id.withoutloginbutton:
                User user = new User();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                user.setAuthType("guestuser");
                SharedPreferenceUtils.getInstance(this).putString(Constants.KEY_USER, new Gson().toJson(user));
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void initializeView() {
        Button googleSignInButton = findViewById(R.id.googlebutton);
        Button facebookSignInButton = findViewById(R.id.facebook_login_button);
        Button continueWithOutLogin = findViewById(R.id.withoutloginbutton);

        googleSignInButton.setOnClickListener(this);
        facebookSignInButton.setOnClickListener(this);
        continueWithOutLogin.setOnClickListener(this);
    }

    @Override
    public void onLoginSuccess(FirebaseUser user) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFailure() {
        showAlert(null, "Login failed", "Ok", null, null);
    }


}
