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
import com.remotearthsolutions.expensetracker.utils.Constants;

public class ETFirebaseMessegingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (((ApplicationObject) getApplicationContext()).isActivityVisible()) {
            Activity activity = ((ApplicationObject) getApplicationContext()).getCurrentActivity();
            activity.runOnUiThread(() -> showAlertDialog(activity, remoteMessage.getNotification().getBody()));
        } else {
            showNotification(remoteMessage);
        }
    }

    private void showAlertDialog(Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(getString(R.string.remotearth_notification));
        builder.setMessage(message);
        builder.setIcon(R.mipmap.ic_logo);
        builder.setCancelable(true);
        builder.setNegativeButton(getString(R.string.ok), (dialog, id) -> dialog.cancel());

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showNotification(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (remoteMessage.getData().size() > 0) {
            String message = remoteMessage.getData().get(getString(R.string.message));
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_MESSAGE, message);
            intent.putExtras(bundle);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(getString(R.string.remotearth_notification));
        builder.setContentText(remoteMessage.getNotification().getBody());
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.mipmap.ic_logo);
        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
