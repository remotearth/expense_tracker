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
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
        interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {

                interstitialAd.show();

            }
        });
    }

}
