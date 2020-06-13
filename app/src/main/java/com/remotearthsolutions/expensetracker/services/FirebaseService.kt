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

    fun uploadToFirebaseStorage(
        user: String,
        dataBase64Encrypted: String,
        path: String,
        onSuccess: (() -> Unit)?,
        onFailure: (() -> Unit)?
    )

    fun uploadToFirebaseStorage(
        user: String,
        dataMap: Map<String, String>,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    )

    fun downloadFromFirebaseStorage(
        user: String,
        onSuccess: (Map<String, String>) -> Unit,
        onFailure: (String) -> Unit
    )

    interface Callback {
        fun onFirebaseSigninSuccess(user: FirebaseUser?)
        fun onFirebaseSigninFailure(message: String?)
    }

    companion object {
        val KEY_EXPENSES = "expenses"
        val KEY_CATEGORIES = "categories"
        val KEY_ACCOUNTS = "accounts"
    }
}