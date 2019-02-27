package com.remotearthsolutions.expensetracker.utils;

import android.content.Context;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.remotearthsolutions.expensetracker.BuildConfig;
import com.remotearthsolutions.expensetracker.R;

public class AdmobUtils {

    private Context context;
    private InterstitialAd interstitialAd;


    public AdmobUtils(Context context) {
        this.context = context;
    }


    public void showInterstitialAds() {
        interstitialAd = new InterstitialAd(context);
        if (BuildConfig.DEBUG) {
            interstitialAd.setAdUnitId(context.getString(R.string.admob_test_ad_id));
        } else {
            interstitialAd.setAdUnitId(context.getString(R.string.admob_ad_id));
        }
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
        interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {

                interstitialAd.show();

            }
        });
    }

}
