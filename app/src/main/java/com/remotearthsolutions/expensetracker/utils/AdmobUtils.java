package com.remotearthsolutions.expensetracker.utils;

import android.content.Context;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.remotearthsolutions.expensetracker.R;

public class AdmobUtils {

    private Context context;

    public AdmobUtils(Context context) {
        this.context = context;
    }


    public void showInterstitialAds(InterstitialAd adView)
    {

        adView = new InterstitialAd(context);
        adView.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("FD81953EDAC0685EA8F222A121FCC895").build();
        adView.loadAd(adRequest);
        if (adView.isLoaded()) {
            adView.show();
        } else {
            Toast.makeText(context, "The interstitial wasn't loaded yet.",Toast.LENGTH_SHORT).show();
        }
        adView.show();

    }


}
