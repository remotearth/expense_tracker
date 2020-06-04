package com.remotearthsolutions.expensetracker.viewmodels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.contracts.LoginContract
import com.remotearthsolutions.expensetracker.services.FacebookService
import com.remotearthsolutions.expensetracker.services.FacebookService.CallBack
import com.remotearthsolutions.expensetracker.services.FirebaseService
import com.remotearthsolutions.expensetracker.services.GoogleService

class LoginViewModel(
    private val context: Context,
    private val view: LoginContract.View,
    private val googleService: GoogleService,
    private val facebookService: FacebookService,
    private val firebaseService: FirebaseService
) : ViewModel(), CallBack, FirebaseService.Callback,
    GoogleService.Callback {
    fun init() {
        view.initializeView()
        googleService.initializeGoogleSigninClient()
        facebookService.facebookCallbackInitialize()
    }

    fun startFacebookLogin() {
        if (view.isDeviceOnline) {
            facebookService.startFacebookLogin(this)
        } else {
            view.showAlert(
                context.getString(R.string.warning),
                context.getString(R.string.no_net_connection),
                context.getString(R.string.ok), null, null, null
            )
        }
    }

    val facebookCallbackManager: CallbackManager?
        get() = facebookService.facebookCallbackManager

    fun startGoogleLogin() {
        if (view.isDeviceOnline) {
            view.loadUserEmails()
        } else {
            view.showAlert(
                context.getString(R.string.warning),
                context.getString(R.string.no_net_connection),
                context.getString(R.string.ok), null, null, null
            )
        }
    }

    fun googleLoginWithIntent(data: Intent?) {
        googleService.startGoogleLogin(data, this)
    }

    val googleSignInClient: GoogleSignInClient?
        get() = googleService.googleSignInClient

    override fun onFirebaseSigninSuccess(user: FirebaseUser?) {
        view.hideProgress()
        view.onLoginSuccess(user)
    }

    override fun onFirebaseSigninFailure(message: String?) {
        view.hideProgress()
        view.onLoginFailure()
        view.showAlert(null, message, context.getString(R.string.ok), null, null, null)
    }

    override fun onSocialLoginSuccess(credential: AuthCredential?) {
        view.showProgress(context.getString(R.string.please_wait))
        firebaseService.signinWithCredential(credential, this)
    }

    override fun onSocialLoginFailure(message: String?) {
        view.showAlert(null, message, context.getString(R.string.ok), null, null, null)
    }

    override fun onFacebookLoginCancel() {
        view.onLoginFailure()
    }

}