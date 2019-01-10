package com.remotearthsolutions.expensetracker.services;

import android.content.Context;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.remotearthsolutions.expensetracker.R;

public class GoogleSigninServiceImpl implements GoogleSigninService {

    private GoogleSignInClient mGoogleSignInClient;
    private Context context;

    public GoogleSigninServiceImpl(Context context) {
        this.context = context;
    }

    @Override
    public void initializeGoogleSigninClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    @Override
    public GoogleSignInClient getGoogleSignInClient() {
        return mGoogleSignInClient;
    }


}
