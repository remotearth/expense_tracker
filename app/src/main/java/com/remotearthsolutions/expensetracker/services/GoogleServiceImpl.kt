package com.remotearthsolutions.expensetracker.services

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.utils.Constants

class GoogleServiceImpl(private val context: Context) : GoogleService {
    override var googleSignInClient: GoogleSignInClient? = null
        private set

    override fun initializeGoogleSigninClient() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(GOOGLE_CLIENT_ID)
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    override fun startGoogleLogin(
        data: Intent?,
        callback: GoogleService.Callback?
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            val token = account!!.idToken
            val credential = GoogleAuthProvider.getCredential(token, null)
            callback!!.onSocialLoginSuccess(credential)
        } catch (e: ApiException) {
            e.printStackTrace()
            FirebaseCrashlytics.getInstance().recordException(e)
            callback!!.onSocialLoginFailure(context.getString(R.string.google_signin_failed))
        }
    }

    companion object {
        private const val GOOGLE_CLIENT_ID =
            Constants.KEY_GOOGLE_CLIENT_ID
    }

}