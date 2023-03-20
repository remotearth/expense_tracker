package com.remotearthsolutions.expensetracker.utils

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging

class FirebaseUtils {

    companion object {

        const val TOPIC_GENERAL_USER = "general_user"
        const val TOPIC_PAID_USER = "paid_user"

        fun subscribeToTopic(topicName: String) {
            FirebaseMessaging.getInstance().subscribeToTopic(topicName)
        }

        fun logEvent(context: Context?, eventName: String, properties: Map<String, String>) {
            val bundle = Bundle()
            for ((key, value) in properties.entries.iterator()) {
                bundle.putString(key, value)
            }
            context?.let {
                FirebaseAnalytics.getInstance(context).logEvent(eventName, bundle)
            }
        }
    }
}