package com.remotearthsolutions.expensetracker.services

import android.app.Activity
import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.remotearthsolutions.expensetracker.R

class FirebaseServiceImpl(private val context: Context) : FirebaseService {
    private val COLLECTION_NAME = "expensebackup"
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

    override fun sync() {

    }

    override fun uploadToFireStore(
        user: String,
        dataMap: Map<String, String>,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
            .document(user)
            .set(dataMap)
            .addOnSuccessListener { onSuccess.invoke() }
            .addOnFailureListener { onFailure.invoke() }
    }

    override fun downloadFromCloud(
        user: String,
        onSuccess: (Map<String, String>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val docRef = FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
            .document(user)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val map = HashMap<String,String>()
                    document.data?.forEach {
                        map[it.key] = it.value.toString()
                    }
                    onSuccess(map)
                } else {
                    onFailure("No data available to download for this account")
                }
            }
            .addOnFailureListener {
                onFailure("Something went wrong. Please try again later.")
            }
    }


    init {
        mAuth = FirebaseAuth.getInstance()
    }
}