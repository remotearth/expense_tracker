package com.remotearthsolutions.expensetracker.services

import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.ApplicationObject
import com.remotearthsolutions.expensetracker.activities.main.MainActivity
import com.remotearthsolutions.expensetracker.utils.Constants

class ETFirebaseMessegingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("Firebase token: $token")
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if ((applicationContext as ApplicationObject).isActivityVisible) {
            val activity = (applicationContext as ApplicationObject).currentActivity
            activity!!.runOnUiThread {
                showAlertDialog(
                    activity,
                    remoteMessage.notification!!.body
                )
            }
        } else {
            showNotification(remoteMessage)
        }
    }

    private fun showAlertDialog(activity: Activity?, message: String?) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.remotearth_notification))
        builder.setMessage(message)
        builder.setIcon(R.mipmap.ic_logo)
        builder.setCancelable(true)
        builder.setNegativeButton(getString(R.string.ok)) { dialog: DialogInterface, _: Int -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    private fun showNotification(remoteMessage: RemoteMessage) {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        if (remoteMessage.data.isNotEmpty()) {
            val message = remoteMessage.data[getString(R.string.message)]
            val bundle = Bundle()
            bundle.putString(
                Constants.KEY_MESSAGE,
                message
            )
            intent.putExtras(bundle)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val builder = NotificationCompat.Builder(this)
        builder.setContentTitle(getString(R.string.remotearth_notification))
        builder.setContentText(remoteMessage.notification!!.body)
        builder.setAutoCancel(true)
        builder.setSmallIcon(R.mipmap.ic_logo)
        builder.setContentIntent(pendingIntent)
        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, builder.build())
    }
}