package com.remotearthsolutions.expensetracker.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.contracts.LoginContract;
import com.remotearthsolutions.expensetracker.services.FacebookServiceImpl;
import com.remotearthsolutions.expensetracker.services.FirebaseServiceImpl;
import com.remotearthsolutions.expensetracker.services.GoogleServiceImpl;
import com.remotearthsolutions.expensetracker.services.InternetCheckerServiceImpl;
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
            viewModel.googleLoginWithIntent(data);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.googlebutton:
                viewModel.startGoogleLogin();
                break;

            case R.id.facebook_login_button:
                viewModel.startFacebookLogin();
                break;

            case R.id.withoutloginbutton:
                onLoginSuccess(null);
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
        
        if (SharedPreferenceUtils.getInstance(this).getBoolean(Constants.PREF_ISFIRSTTIMEVISITED, false)) {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        else{
            Intent intent = new Intent(this, CurrencySelectionActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onLoginFailure() {
        showAlert(null, "Login failed", "Ok", null, null);
    }

    @Override
    public void loadUserEmails() {
        GoogleSignInClient mGoogleSignInClient = viewModel.getGoogleSignInClient();

        if (mGoogleSignInClient == null) {
            Log.w(TAG, "mGoogleSignInClient must be initialized");
            return;
        }

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE_SIGNIN_INTENT);
    }


}
