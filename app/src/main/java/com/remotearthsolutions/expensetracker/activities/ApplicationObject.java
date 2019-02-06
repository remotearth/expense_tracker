package com.remotearthsolutions.expensetracker.activities;

import android.app.Application;
import androidx.multidex.MultiDexApplication;
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils;

public class ApplicationObject extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferenceUtils.getInstance(this);
    }
}
