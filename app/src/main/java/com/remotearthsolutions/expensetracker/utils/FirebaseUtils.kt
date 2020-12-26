package com.remotearthsolutions.expensetracker.utils

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging

class FirebaseUtils {

    companion object {
        fun logFirebaseMessagingToken() {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                val token = task.result
                token?.let{
                    with(AnalyticsManager) { logEvent(it) }
                    println("FirebaseToken: $it")
                }
            })
        }

        fun logFirebaseUserId() {
            FirebaseAuth.getInstance().currentUser?.let {
                with(AnalyticsManager) { logEvent(it.uid) }
                println("Firebase UserId: ${it.uid}")
            }
        }


    }
}