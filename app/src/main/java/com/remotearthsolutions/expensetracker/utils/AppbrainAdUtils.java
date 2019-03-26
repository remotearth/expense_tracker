package com.remotearthsolutions.expensetracker.utils;

import android.app.Activity;
import com.appbrain.AdId;
import com.appbrain.InterstitialBuilder;
import com.appbrain.InterstitialListener;
import com.remotearthsolutions.expensetracker.activities.ApplicationObject;

public class AppbrainAdUtils {

    private static AppbrainAdUtils instance;
    private Activity activity;
    private InterstitialBuilder interstitialBuilder;

    public static AppbrainAdUtils getInstance(Activity activity) {
        if (instance == null) {
            instance = new AppbrainAdUtils(activity);
        }

        return instance;
    }

    private AppbrainAdUtils(Activity activity) {
        this.activity = activity;
    }

    public void showAds() {

        interstitialBuilder = InterstitialBuilder.create()
                .setAdId(AdId.LEVEL_COMPLETE)
                .setListener(new InterstitialListener() {
                    @Override
                    public void onPresented() {

                    }

                    @Override
                    public void onClick() {

                    }

                    @Override
                    public void onDismissed(boolean b) {

                    }

                    @Override
                    public void onAdLoaded() {
                        ApplicationObject app = (ApplicationObject) activity.getApplication();
                        if (app.isActivityVisible() && app.isAppShouldShowAds()) {
                            interstitialBuilder.show(activity);
                            FabricAnswersUtils.logCustom("Appbrain Ad shown");
                        }
                    }

                    @Override
                    public void onAdFailedToLoad(InterstitialError interstitialError) {

                    }
                })
                .preload(activity);

    }
}
