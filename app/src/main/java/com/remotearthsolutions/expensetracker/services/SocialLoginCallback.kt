package com.remotearthsolutions.expensetracker.services

import com.google.firebase.auth.AuthCredential

interface SocialLoginCallback {
    fun onSocialLoginSuccess(credential: AuthCredential?)
    fun onSocialLoginFailure(message: String?)
}