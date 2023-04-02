package com.remotearthsolutions.expensetracker.services

import android.app.Activity
import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.crashlytics.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.ktx.storage
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.utils.Constants
import java.util.*
import kotlin.collections.HashMap

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
                if (BuildConfig.DEBUG) {
                    it.printStackTrace()
                }
                FirebaseCrashlytics.getInstance().recordException(it)
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
                if (BuildConfig.DEBUG) {
                    it.printStackTrace()
                }
                FirebaseCrashlytics.getInstance().recordException(it)
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

    override fun uploadToFirebaseStorage(
        user: String,
        dataBase64Encrypted: String,
        path: String,
        onSuccess: (() -> Unit)?,
        onFailure: (() -> Unit)?
    ) {
        val storage = Firebase.storage.reference.child("$path$user.txt")
        storage.putBytes(dataBase64Encrypted.toByteArray(charset(Constants.KEY_UTF_VERSION)))
            .addOnSuccessListener { onSuccess?.invoke() }
            .addOnFailureListener { exception ->
                if (BuildConfig.DEBUG) {
                    exception.printStackTrace()
                }
                FirebaseCrashlytics.getInstance().recordException(exception)
                onFailure?.invoke()
            }
    }

    override fun downloadFromFirebaseStorage(
        user: String,
        onSuccess: (Map<String, String>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val storage = Firebase.storage.reference.child("exporteddata/$user.txt")
        storage.getBytes(10 * 1024 * 1024)
            .addOnSuccessListener {
                if (it != null && it.isNotEmpty()) {
                    val str = String(it)
                    val stringTokenizer = StringTokenizer(str, "|")
                    val dataMap = HashMap<String, String>()
                    dataMap[FirebaseService.KEY_EXPENSES] = stringTokenizer.nextToken()
                    dataMap[FirebaseService.KEY_CATEGORIES] = stringTokenizer.nextToken()
                    dataMap[FirebaseService.KEY_ACCOUNTS] = stringTokenizer.nextToken()
                    onSuccess.invoke(dataMap)
                } else {
                    onFailure(context.getString(R.string.data_not_available_to_download))
                }
            }
            .addOnFailureListener { exception ->
                if (BuildConfig.DEBUG) {
                    exception.printStackTrace()
                }
                if (exception is StorageException) {
                    onFailure.invoke(context.getString(R.string.data_not_available_to_download))
                } else {
                    onFailure.invoke(context.getString(R.string.something_went_wrong))
                }
            }
    }


    init {
        mAuth = FirebaseAuth.getInstance()
    }
}