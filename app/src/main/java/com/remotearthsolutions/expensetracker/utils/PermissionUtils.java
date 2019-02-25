package com.remotearthsolutions.expensetracker.utils;

import android.Manifest;
import android.app.Activity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.PermissionListener;

public class PermissionUtils {

    public void writeExternalStoragePermission(Activity activity, PermissionListener permissionListener) {
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(permissionListener)
                .check();
    }


}
