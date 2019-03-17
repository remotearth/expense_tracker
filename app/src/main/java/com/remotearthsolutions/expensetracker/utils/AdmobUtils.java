package com.remotearthsolutions.expensetracker.utils;

import android.app.Activity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.remotearthsolutions.expensetracker.BuildConfig;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.activities.ApplicationObject;

public final class AdmobUtils {

    private Activity activity;
    private InterstitialAd interstitialAd;
    private static AdmobUtils instance;
    private boolean appShouldShowAds;

    public static AdmobUtils getInstance(Activity activity) {
        if (instance == null) {
            instance = new AdmobUtils(activity);
        }

        return instance;
    }

    private AdmobUtils(Activity activity) {
        this.activity = activity;
    }

    public void showInterstitialAds() {
        interstitialAd = new InterstitialAd(activity);
        if (BuildConfig.DEBUG) {
            interstitialAd.setAdUnitId(activity.getString(R.string.admob_test_ad_id));
        } else {
            interstitialAd.setAdUnitId(activity.getString(R.string.admob_ad_id));
        }
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
        interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {

                if (((ApplicationObject) activity.getApplication()).isActivityVisible() && appShouldShowAds) {
                    interstitialAd.show();
                }

            }
        });
    }

    public void appShouldShowAds(boolean state) {
        this.appShouldShowAds = state;
    }

}
