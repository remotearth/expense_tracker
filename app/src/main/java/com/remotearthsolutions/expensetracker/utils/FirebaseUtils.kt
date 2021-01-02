package com.remotearthsolutions.expensetracker.utils

import android.util.Log
import com.amplitude.api.Amplitude
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
                token?.let {
                    with(AnalyticsManager) { logEvent(it) }
                    println("FirebaseToken: $it")
                }
            })
        }

        fun setFirebaseUidAsAmplitudeUid() {
            FirebaseAuth.getInstance().currentUser?.let {
                print("Firebase UserId: ${it.uid}")
                Amplitude.getInstance().setUserId(it.uid)
            }
        }


    }
}