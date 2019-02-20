package com.remotearthsolutions.expensetracker.utils;

import android.content.Context;
import android.widget.Toast;
import com.google.android.gms.ads.*;
import com.remotearthsolutions.expensetracker.R;

public class AdmobUtils {

    private Context context;
    private InterstitialAd interstitialAd;


    public AdmobUtils(Context context) {
        this.context = context;
    }


    public void showInterstitialAds()
    {
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(context.getString(R.string.appunitid));
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
        interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {

              initiate();

            }
        });
    }

    private void initiate()
    {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }


}
