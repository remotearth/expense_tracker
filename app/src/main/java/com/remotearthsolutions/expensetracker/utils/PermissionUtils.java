package com.remotearthsolutions.expensetracker.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.PermissionListener;

public class PermissionUtils {

    public void writeExternalStoragePermission(Activity activity, PermissionListener permissionListener) {
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(permissionListener)
                .check();
    }

    public void readExternalStoragePermission(Activity activity, PermissionListener permissionListener) {
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(permissionListener)
                .check();
    }

    public void showSettingsDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Writing to external storage permission is needed to export data. You can still enable this permission from app settings.");

        builder.setPositiveButton("Go To Setting", (dialog, which) -> {
            dialog.cancel();
            openSettings(activity);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void openSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, 101);
    }


}
