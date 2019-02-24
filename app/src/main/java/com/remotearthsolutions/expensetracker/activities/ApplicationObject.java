package com.remotearthsolutions.expensetracker.activities;

import android.app.Application;
import androidx.multidex.MultiDexApplication;
import com.remotearthsolutions.expensetracker.utils.Constants;
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils;
import org.solovyev.android.checkout.Billing;

public class ApplicationObject extends MultiDexApplication {

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
    }

    public static ApplicationObject get() {
        return sInstance;
    }

    public Billing getBilling() {
        return mBilling;
    }
}
