package com.remotearthsolutions.expensetracker.services

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInClient

interface GoogleService {
    fun initializeGoogleSigninClient()
    fun startGoogleLogin(
        data: Intent?,
        callback: Callback?
    )

    val googleSignInClient: GoogleSignInClient?

    interface Callback : SocialLoginCallback
}