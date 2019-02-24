package com.remotearthsolutions.expensetracker.activities;

import androidx.multidex.MultiDexApplication;
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils;

public class ApplicationObject extends MultiDexApplication {

    private static boolean activityVisible;

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferenceUtils.getInstance(this);
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }
}
