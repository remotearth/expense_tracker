package com.remotearthsolutions.expensetracker.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.activities.ApplicationObject;
import com.remotearthsolutions.expensetracker.activities.MainActivity;

public class MyFirebaseMessegingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (ApplicationObject.isActivityVisible()) {

            Activity activity = ((ApplicationObject) getApplicationContext()).getCurrentActivity();

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showAlertDialog(activity, remoteMessage.getNotification().getBody());
                }
            });
        } else {
            showNotification(remoteMessage);
        }
    }

    public void showAlertDialog(Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("Remotearth Notification");
        builder.setMessage(message);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(true);
        builder.setNegativeButton("OK", (dialog, id) -> dialog.cancel());

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showNotification(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (remoteMessage.getData().size() > 0) {
            String message = remoteMessage.getData().get("message");
            Bundle bundle = new Bundle();
            bundle.putString("message", message);
            intent.putExtras(bundle);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Remotearth Notification");
        builder.setContentText(remoteMessage.getNotification().getBody());
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
