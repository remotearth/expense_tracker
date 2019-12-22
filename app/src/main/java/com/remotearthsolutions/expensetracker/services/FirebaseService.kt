package com.remotearthsolutions.expensetracker.services

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser

interface FirebaseService {
    fun signinWithCredential(
        token: AuthCredential?,
        callback: Callback?
    )

    fun signinAnonymously(callback: Callback?)
    val user: FirebaseUser?
    fun logout()
    interface Callback {
        fun onFirebaseSigninSuccess(user: FirebaseUser?)
        fun onFirebaseSigninFailure(message: String?)
    }
}