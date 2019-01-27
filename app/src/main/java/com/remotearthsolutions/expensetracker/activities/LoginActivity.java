package com.remotearthsolutions.expensetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.contracts.LoginContract;
import com.remotearthsolutions.expensetracker.viewmodels.LoginViewModel;
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.LoginViewModelFactory;
import com.remotearthsolutions.expensetracker.services.FacebookServiceImpl;
import com.remotearthsolutions.expensetracker.services.FirebaseServiceImpl;
import com.remotearthsolutions.expensetracker.services.GoogleServiceImpl;

public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginContract.View {

    private static final String TAG = LoginActivity.class.getName();
    private static final int REQUEST_CODE_GOOGLE_SIGNIN_INTENT = 101;

    private LoginViewModel presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = ViewModelProviders.of(this,
                new LoginViewModelFactory(this, new GoogleServiceImpl(this), new FacebookServiceImpl(this), new FirebaseServiceImpl(this))).
                get(LoginViewModel.class);
        presenter.init();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        presenter.getFacebookCallbackManager().onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GOOGLE_SIGNIN_INTENT) {
            presenter.startGoogleLogin(data);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.googlebutton:
                GoogleSignInClient mGoogleSignInClient = presenter.getGoogleSignInClient();

                if (mGoogleSignInClient == null) {
                    Log.w(TAG, "mGoogleSignInClient must be initialized");
                    return;
                }

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE_SIGNIN_INTENT);
                break;

            case R.id.facebook_login_button:
                presenter.startFacebookLogin();
                break;
        }

    }

    @Override
    public void initializeView() {
        Button googleSignInButton = findViewById(R.id.googlebutton);
        Button facebookSignInButton = findViewById(R.id.facebook_login_button);

        googleSignInButton.setOnClickListener(this);
        facebookSignInButton.setOnClickListener(this);
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
