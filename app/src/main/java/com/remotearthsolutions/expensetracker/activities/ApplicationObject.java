package com.remotearthsolutions.expensetracker.activities;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import androidx.multidex.MultiDexApplication;
import com.google.android.gms.ads.MobileAds;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.utils.Constants;
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils;
import org.solovyev.android.checkout.Billing;

public class ApplicationObject extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {

    private boolean activityVisible;
    private Activity currentActivity;

    private static ApplicationObject sInstance;

    public ApplicationObject() {
        sInstance = this;
    }

    private final Billing mBilling = new Billing(this, new Billing.DefaultConfiguration() {
        @Override
        public String getPublicKey() {
            return Constants.LICENSE_KEY;
        }
    });

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferenceUtils.getInstance(this);
        registerActivityLifecycleCallbacks(this);
    }

    public boolean isActivityVisible() {
        return activityVisible;
    }

    public void activityResumed() {
        activityVisible = true;
    }

    public void activityPaused() {
        activityVisible = false;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        currentActivity = null;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public static ApplicationObject get() {
        return sInstance;
    }

    public Billing getBilling() {
        return mBilling;
    }
}
