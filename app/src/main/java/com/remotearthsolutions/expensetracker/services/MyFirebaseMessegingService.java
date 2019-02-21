package com.remotearthsolutions.expensetracker.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.activities.MainActivity;

public class MyFirebaseMessegingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Intent intent = new Intent(this, MainActivity.class);
        if (remoteMessage.getData().size() > 0)
        {
            String message = remoteMessage.getData().get("message");
            Bundle bundle = new Bundle();
            bundle.putString("message",message);
            intent.putExtras(bundle);

        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Remotearth Notification");
        builder.setContentText(remoteMessage.getNotification().getBody());
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());
    }

    public void showNotificationAlert(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle("New Notification");
        builder.setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();


    }
}
