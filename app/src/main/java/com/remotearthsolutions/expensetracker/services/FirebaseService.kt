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
    fun sync()
    fun uploadToFireStore(
        user: String,
        dataMap: Map<String, String>,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    )

    fun downloadFromCloud(user: String, onSuccess: (Map<String, String>) -> Unit, onFailure: (String) -> Unit)

    interface Callback {
        fun onFirebaseSigninSuccess(user: FirebaseUser?)
        fun onFirebaseSigninFailure(message: String?)
    }
}