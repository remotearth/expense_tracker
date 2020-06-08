package com.remotearthsolutions.expensetracker.services

import android.app.Activity
import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
                it.printStackTrace()
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
                it.printStackTrace()
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

//    override fun uploadToFireStore(
//        user: String,
//        dataMap: Map<String, String>,
//        onSuccess: () -> Unit,
//        onFailure: () -> Unit
//    ) {
//        FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
//            .document(user)
//            .set(dataMap)
//            .addOnSuccessListener { onSuccess.invoke() }
//            .addOnFailureListener { exception ->
//                exception.printStackTrace()
//                onFailure.invoke()
//            }
//    }
//
//    override fun downloadFromCloud(
//        user: String,
//        onSuccess: (Map<String, String>) -> Unit,
//        onFailure: (String) -> Unit
//    ) {
//        val docRef = FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
//            .document(user)
//        docRef.get()
//            .addOnSuccessListener { document ->
//                if (document != null) {
//                    val map = HashMap<String, String>()
//                    document.data?.forEach {
//                        map[it.key] = it.value.toString()
//                    }
//                    onSuccess(map)
//                } else {
//                    onFailure(context.getString(R.string.no_data_available_to_download))
//                }
//            }
//            .addOnFailureListener {
//                it.printStackTrace()
//                onFailure(context.getString(R.string.something_went_wrong))
//            }
//    }

    override fun uploadToFirebaseStorage(
        user: String, dataMap: Map<String, String>, onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        val str =
            "${dataMap[FirebaseService.KEY_EXPENSES]}|${dataMap[FirebaseService.KEY_CATEGORIES]}|${dataMap[FirebaseService.KEY_ACCOUNTS]}"

        val storage = Firebase.storage.reference.child("exporteddata/$user.txt")
        storage.putBytes(str.toByteArray(charset(Constants.KEY_UTF_VERSION)))
            .addOnSuccessListener { onSuccess.invoke() }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                onFailure.invoke()
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