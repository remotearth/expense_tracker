package com.remotearthsolutions.expensetracker.services

import android.app.Activity
import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.remotearthsolutions.expensetracker.R

class FirebaseServiceImpl(private val context: Context) : FirebaseService {
    private val mAuth: FirebaseAuth?
    override fun signinWithCredential(
        token: AuthCredential?,
        callback: FirebaseService.Callback?
    ) {
        mAuth!!.signInWithCredential(token!!)
            .addOnCompleteListener(
                (context as Activity)
            ) { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    callback!!.onFirebaseSigninSuccess(task.result!!.user)
                } else {
                    callback!!.onFirebaseSigninFailure(context.getString(R.string.Authentication_with_Firebase_is_failed))
                }
            }
            .addOnFailureListener(context) {
                callback!!.onFirebaseSigninFailure(context.getString(R.string.Authentication_with_Firebase_is_failed))
            }
    }

    override fun signinAnonymously(callback: FirebaseService.Callback?) {
        mAuth!!.signInAnonymously()
            .addOnCompleteListener(
                (context as Activity)
            ) { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    callback!!.onFirebaseSigninSuccess(task.result!!.user)
                } else {
                    callback!!.onFirebaseSigninFailure(context.getString(R.string.Authentication_with_Firebase_is_failed))
                }
            }.addOnFailureListener(
                context
            ) {
                callback!!.onFirebaseSigninFailure(
                    context.getString(R.string.Authentication_with_Firebase_is_failed)
                )
            }
    }

    override val user: FirebaseUser?
        get() = mAuth?.currentUser

    override fun logout() {
        mAuth?.signOut()
    }

    init {
        mAuth = FirebaseAuth.getInstance()
    }
}