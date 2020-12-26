package com.remotearthsolutions.expensetracker.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.installations.FirebaseInstallations

class FirebaseUtils {

    companion object {
        fun logFirebaseInstallationToken() {
            FirebaseInstallations.getInstance().id.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let {
                        with(AnalyticsManager) { logEvent(it) }
                        println("FirebaseToken: $it")
                    }
                } else {
                    Log.e("Installations", "Unable to get firebase Installation ID")
                }
            }
        }

        fun logFirebaseUserId() {
            FirebaseAuth.getInstance().currentUser?.let {
                with(AnalyticsManager) { logEvent(it.uid) }
                println("Firebase UserId: ${it.uid}")
            }
        }
    }
}