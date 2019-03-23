package com.remotearthsolutions.expensetracker.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.PermissionListener;
import com.remotearthsolutions.expensetracker.R;

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
        builder.setMessage(activity.getString(R.string.storage_permission_needed_still_can_enable_from_settings));
        builder.setPositiveButton(activity.getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings(activity);
        });
        builder.setNegativeButton(activity.getString(R.string.cancel), (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void openSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts(Constants.KEY_PACKAGE, activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, 101);
    }


}
