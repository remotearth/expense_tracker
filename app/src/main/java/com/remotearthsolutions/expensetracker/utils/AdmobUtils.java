package com.remotearthsolutions.expensetracker.utils;

import android.content.Context;
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

    public void showBannerAds(AdView adView)
    {
        MobileAds.initialize(context,"ca-app-pub-3940256099942544~3347511713");
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("FD81953EDAC0685EA8F222A121FCC895").build();
        adView.loadAd(adRequest);

    }

    public void showInterstitialAds(InterstitialAd adView)
    {
        MobileAds.initialize(context,context.getString(R.string.appid));
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("FD81953EDAC0685EA8F222A121FCC895").build();
        adView.loadAd(adRequest);

    }


}
