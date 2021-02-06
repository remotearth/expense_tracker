package com.remotearthsolutions.expensetracker.utils

import android.content.Context
import android.os.Bundle
import com.amplitude.api.Amplitude
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

class FirebaseUtils {

    companion object {

        const val TOPIC_GENERAL_USER = "general_user"
        const val TOPIC_PAID_USER = "paid_user"

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

        fun subscribeToTopic(topicName: String) {
            FirebaseMessaging.getInstance().subscribeToTopic(topicName)
        }

        fun logEvent(context: Context?, eventName: String) {
            val bundle = Bundle();
            bundle.putString("testKey", "anyvalue")
            bundle.putInt("testIntKey", 100)
            context?.let {
                FirebaseAnalytics.getInstance(context).logEvent(eventName, bundle)
            }
        }

    }
}