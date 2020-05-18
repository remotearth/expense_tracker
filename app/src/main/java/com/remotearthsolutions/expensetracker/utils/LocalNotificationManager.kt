package com.remotearthsolutions.expensetracker.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.main.MainActivity
import kotlin.random.Random

object LocalNotificationManager {
    private const val CHANNEL_ID = "EXPENSE_TRACKER_NOTIFICATION_CHANNEL_ID"
    fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(
        context: Context,
        title: String,
        content: String,
        pendingIntent: PendingIntent?
    ) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_logo)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        if (pendingIntent == null) {
            val intent = Intent(context, MainActivity::class.java)
            val defaultPendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
            builder.setContentIntent(defaultPendingIntent)
        } else {
            builder.setContentIntent(pendingIntent)
        }
        with(NotificationManagerCompat.from(context)) {
            notify(Random(10).nextInt(), builder.build())
        }
    }
}
