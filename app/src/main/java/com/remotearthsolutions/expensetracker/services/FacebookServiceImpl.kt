package com.remotearthsolutions.expensetracker.services

import android.app.Activity
import android.content.Context
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.ProfilePictureView
import com.google.firebase.auth.FacebookAuthProvider
import com.remotearthsolutions.expensetracker.services.FacebookService.CallBack

class FacebookServiceImpl(private val context: Context) : FacebookService {
    override var facebookCallbackManager: CallbackManager? = null
        private set

    override fun facebookCallbackInitialize() {
        facebookCallbackManager = CallbackManager.Factory.create()
    }

    override fun startFacebookLogin(callBack: CallBack?) {
        LoginManager.getInstance().logInWithReadPermissions(
            context as Activity,
            listOf("email", "public_profile")
        )
        LoginManager.getInstance()
            .registerCallback(facebookCallbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d("", "facebook:onSuccess:$loginResult")
                    val token = loginResult.accessToken.token
                    val credential = FacebookAuthProvider.getCredential(token)
                    callBack!!.onSocialLoginSuccess(credential)
                }

                override fun onCancel() {
                    Log.d(ProfilePictureView.TAG, "facebook:onCancel")
                    callBack!!.onFacebookLoginCancel()
                }

                override fun onError(error: FacebookException) {
                    Log.d(ProfilePictureView.TAG, "facebook:onError", error)
                    callBack!!.onSocialLoginFailure(error.message)
                }
            })
    }

}