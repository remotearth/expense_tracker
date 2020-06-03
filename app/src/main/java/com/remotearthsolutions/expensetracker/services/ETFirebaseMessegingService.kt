package com.remotearthsolutions.expensetracker.services

import android.app.PendingIntent
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.ApplicationObject
import com.remotearthsolutions.expensetracker.activities.main.MainActivity
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.LocalNotificationManager

class ETFirebaseMessegingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if ((applicationContext as ApplicationObject).isActivityVisible) {
            val activity = (applicationContext as ApplicationObject).currentActivity
            activity!!.runOnUiThread {
                AlertDialogUtils.show(
                    activity,
                    null,
                    remoteMessage.notification!!.body,
                    activity.getString(R.string.ok),
                    null,
                    null
                )
            }
        } else {
            showNotification(remoteMessage)
        }
    }

    private fun showNotification(remoteMessage: RemoteMessage) {
        val intent = Intent(this, MainActivity::class.java)
        if (remoteMessage.data.isNotEmpty()) {
            val message = remoteMessage.data[Constants.KEY_MESSAGE]
            intent.putExtra(Constants.KEY_MESSAGE, message)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        LocalNotificationManager.showNotification(
            this,
            getString(R.string.app_name),
            remoteMessage.notification!!.body!!,
            pendingIntent
        )
    }
}
