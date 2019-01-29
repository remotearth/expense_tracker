package com.remotearthsolutions.expensetracker.activities;

import android.app.Application;
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils;

public class ApplicationObject extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferenceUtils.getInstance(this);
    }
}
